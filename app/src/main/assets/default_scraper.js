// tiktok-scraper.js - TikTok Video Downloader with HD Pipeline
const axios = require('axios');
const puppeteer = require('puppeteer');
const fs = require('fs');
const { exec } = require('child_process');
const path = require('path');
const { createWriteStream } = require('fs');
const { pipeline } = require('stream');
const { promisify } = require('util');
const streamPipeline = promisify(pipeline);

class TikTokScraper {
constructor(options = {}) {
this.options = {
quality: options.quality || '1080p',
watermark: options.watermark || false,
maxRetries: options.maxRetries || 3,
downloadPath: options.downloadPath || './downloads',
proxy: options.proxy || null,
...options
};

this.userAgent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36';  
    this.sessionData = null;  
    this.browser = null;  
}  

// Initialize browser for scraping  
async initBrowser() {  
    if (this.browser) return this.browser;  
      
    this.browser = await puppeteer.launch({  
        headless: 'new',  
        args: [  
            '--no-sandbox',  
            '--disable-setuid-sandbox',  
            '--disable-dev-shm-usage',  
            '--disable-accelerated-2d-canvas',  
            '--disable-gpu',  
            '--window-size=1920,1080'  
        ],  
        ignoreHTTPSErrors: true  
    });  
    return this.browser;  
}  

// Get TikTok video data via internal API  
async getVideoData(url) {  
    const browser = await this.initBrowser();  
    const page = await browser.newPage();  
      
    try {  
        // Set headers to bypass WAF  
        await page.setExtraHTTPHeaders({  
            'Accept-Language': 'en-US,en;q=0.9',  
            'Accept-Encoding': 'gzip, deflate, br',  
            'Cache-Control': 'no-cache',  
            'Pragma': 'no-cache',  
            'User-Agent': this.userAgent,  
            'Sec-Ch-Ua': '"Not_A Brand";v="8", "Chromium";v="120", "Google Chrome";v="120"',  
            'Sec-Ch-Ua-Mobile': '?0',  
            'Sec-Ch-Ua-Platform': '"Windows"'  
        });  
          
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });  
          
        // Wait for video content  
        await page.waitForSelector('video', { timeout: 10000 });  
          
        // Extract data using page.evaluate  
        const data = await page.evaluate(() => {  
            // Get video URL from page  
            const videoElement = document.querySelector('video');  
            const videoUrl = videoElement ? videoElement.src : null;  
              
            // Extract metadata from page  
            const userElement = document.querySelector('[data-e2e="user-name"]');  
            const user = userElement ? userElement.textContent : null;  
              
            const captionElement = document.querySelector('[data-e2e="browse-video-desc"]');  
            const caption = captionElement ? captionElement.textContent : null;  
              
            // Get video ID from URL  
            const url = window.location.href;  
            const match = url.match(/video\/(\d+)/);  
            const videoId = match ? match[1] : null;  
              
            // Extract all video URLs from page (for backup)  
            const videoSources = [];  
            document.querySelectorAll('source').forEach(source => {  
                if (source.src) videoSources.push(source.src);  
            });  
              
            return {  
                videoUrl,  
                videoId,  
                user,  
                caption,  
                videoSources,  
                pageUrl: window.location.href  
            };  
        });  
          
        // If direct video URL found, use it  
        let videoUrls = [];  
          
        if (data.videoUrl) {  
            videoUrls = [data.videoUrl, ...data.videoSources];  
        } else {  
            // Fallback: extract from network requests  
            const networkData = await this.extractFromNetwork(page);  
            if (networkData) {  
                videoUrls = networkData;  
            }  
        }  
          
        // Filter and deduplicate URLs  
        videoUrls = [...new Set(videoUrls.filter(url => url && url.includes('video')))];  

        // Get audio track  
        const audioData = await this.extractAudioData(page);  
          
        // Build video info  
        const videoInfo = {  
            id: data.videoId || this.extractIdFromUrl(url),  
            user: data.user || 'unknown',  
            caption: data.caption || '',  
            videoUrls: videoUrls,  
            audioUrl: audioData?.audioUrl || null,  
            musicTitle: audioData?.musicTitle || null,  
            url: url,  
            quality: this.options.quality,  
            timestamp: new Date().toISOString()  
        };  
          
        return videoInfo;  
          
    } catch (error) {  
        console.error('Error in getVideoData:', error);  
        throw error;  
    } finally {  
        await page.close();  
    }  
}  

// Extract from network requests (fallback method)  
async extractFromNetwork(page) {  
    const videoUrls = [];  
      
    // Intercept requests  
    const client = await page.target().createCDPSession();  
    await client.send('Network.enable');  
      
    const startTime = Date.now();  
      
    return new Promise((resolve) => {  
        const timeout = setTimeout(() => {  
            client.send('Network.disable');  
            resolve(videoUrls);  
        }, 10000);  
          
        client.on('Network.responseReceived', (event) => {  
            const url = event.response.url;  
            if (url && url.includes('video') && !url.includes('manifest')) {  
                videoUrls.push(url);  
                clearTimeout(timeout);  
                client.send('Network.disable');  
                resolve(videoUrls);  
            }  
        });  
    });  
}  

// Extract audio data  
async extractAudioData(page) {  
    try {  
        const audioInfo = await page.evaluate(() => {  
            // Find audio element  
            const audioEl = document.querySelector('[data-e2e="music-name"]');  
            const musicTitle = audioEl ? audioEl.textContent : null;  
              
            // Get audio source  
            const audioSource = document.querySelector('source[type="audio/mpeg"]');  
            const audioUrl = audioSource ? audioSource.src : null;  
              
            return { musicTitle, audioUrl };  
        });  
          
        return audioInfo;  
    } catch {  
        return null;  
    }  
}  

// Extract video ID from URL  
extractIdFromUrl(url) {  
    const patterns = [  
        /video\/(\d+)/,  
        /item_id=(\d+)/,  
        /&item_id=(\d+)/  
    ];  
      
    for (const pattern of patterns) {  
        const match = url.match(pattern);  
        if (match) return match[1];  
    }  
      
    return Date.now().toString();  
}  

// Get best quality video URL with pipeline  
async getBestQualityUrl(videoUrls, quality = '1080p') {  
    if (!videoUrls || videoUrls.length === 0) {  
        throw new Error('No video URLs found');  
    }  
      
    const qualityMap = {  
        '1080p': [1080, 1920],  
        '720p': [720, 1280],  
        '480p': [480, 854],  
        '360p': [360, 640]  
    };  
      
    const targetQuality = qualityMap[quality] || qualityMap['720p'];  
      
    // Sort URLs by quality  
    const sortedUrls = videoUrls.sort((a, b) => {  
        const aQuality = this.extractQualityFromUrl(a);  
        const bQuality = this.extractQualityFromUrl(b);  
        return bQuality - aQuality;  
    });  
      
    // Find best match  
    for (const url of sortedUrls) {  
        const urlQuality = this.extractQualityFromUrl(url);  
        if (urlQuality >= targetQuality[0]) {  
            return url;  
        }  
    }  
      
    // Fallback: return first URL  
    return sortedUrls[0] || videoUrls[0];  
}  

// Extract quality from URL  
extractQualityFromUrl(url) {  
    const patterns = [  
        /(\d+)x(\d+)/,  
        /(\d+)p/,  
        /width=(\d+)/  
    ];  
      
    for (const pattern of patterns) {  
        const match = url.match(pattern);  
        if (match) {  
            const value = parseInt(match[1]);  
            if (!isNaN(value)) {  
                return value;  
            }  
        }  
    }  
      
    return 0;  
}  

// Download video using pipeline  
async downloadVideo(url, outputPath) {  
    try {  
        const response = await axios({  
            method: 'GET',  
            url: url,  
            responseType: 'stream',  
            headers: {  
                'User-Agent': this.userAgent,  
                'Referer': 'https://www.tiktok.com/',  
                'Origin': 'https://www.tiktok.com'  
            },  
            maxRedirects: 5  
        });  
          
        const writer = createWriteStream(outputPath);  
        await streamPipeline(response.data, writer);  
          
        return outputPath;  
    } catch (error) {  
        console.error('Error downloading video:', error);  
        throw error;  
    }  
}  

// Re-encode video for HD quality with FFmpeg  
async reencodeVideo(inputPath, outputPath, quality = '1080p') {  
    return new Promise((resolve, reject) => {  
        const qualitySettings = {  
            '1080p': {  
                scale: '1920:1080',  
                bitrate: '5000k',  
                crf: '18'  
            },  
            '720p': {  
                scale: '1280:720',  
                bitrate: '2500k',  
                crf: '20'  
            },  
            '480p': {  
                scale: '854:480',  
                bitrate: '1200k',  
                crf: '22'  
            }  
        };  
          
        const settings = qualitySettings[quality] || qualitySettings['1080p'];  
          
        const ffmpegCommand = `ffmpeg -i "${inputPath}" -vf "scale=${settings.scale}" -c:v libx264 -preset slow -crf ${settings.crf} -b:v ${settings.bitrate} -c:a aac -b:a 192k -movflags +faststart "${outputPath}"`;  
          
        console.log(`Re-encoding video: ${ffmpegCommand}`);  
          
        exec(ffmpegCommand, { maxBuffer: 1024 * 1024 * 1024 }, (error, stdout, stderr) => {  
            if (error) {  
                console.warn(`Re-encoding warning: ${error.message}`);  
                // If re-encoding fails, just copy the original  
                fs.copyFileSync(inputPath, outputPath);  
                resolve(outputPath);  
                return;  
            }  
            resolve(outputPath);  
        });  
    });  
}  

// Main pipeline: Scrape → Download → Re-encode  
async processVideo(url) {  
    console.log(`[+] Processing: ${url}`);  
      
    try {  
        // Step 1: Scrape video data  
        const videoInfo = await this.getVideoData(url);  
        console.log(`[+] Found video: ${videoInfo.id}`);  
        console.log(`[+] User: ${videoInfo.user}`);  
        console.log(`[+] Caption: ${videoInfo.caption.substring(0, 50)}...`);  
          
        if (videoInfo.videoUrls.length === 0) {  
            throw new Error('No video URLs extracted');  
        }  
          
        // Step 2: Get best quality URL  
        const videoUrl = await this.getBestQualityUrl(videoInfo.videoUrls, this.options.quality);  
        console.log(`[+] Best quality URL found`);  
          
        // Step 3: Create download directory  
        if (!fs.existsSync(this.options.downloadPath)) {  
            fs.mkdirSync(this.options.downloadPath, { recursive: true });  
        }  
          
        // Step 4: Download video  
        const tempFilename = `temp_${videoInfo.id}.mp4`;  
        const tempPath = path.join(this.options.downloadPath, tempFilename);  
          
        console.log(`[+] Downloading video...`);  
        await this.downloadVideo(videoUrl, tempPath);  
        console.log(`[+] Downloaded: ${tempPath}`);  
          
        // Step 5: Re-encode for HD quality  
        const outputFilename = `${videoInfo.user}_${videoInfo.id}.mp4`;  
        const outputPath = path.join(this.options.downloadPath, outputFilename);  
          
        console.log(`[+] Re-encoding for ${this.options.quality}...`);  
        await this.reencodeVideo(tempPath, outputPath, this.options.quality);  
        console.log(`[+] Re-encoded: ${outputPath}`);  
          
        // Step 6: Cleanup temp file  
        if (fs.existsSync(tempPath)) {  
            fs.unlinkSync(tempPath);  
        }  
          
        // Step 7: Return result  
        return {  
            success: true,  
            videoId: videoInfo.id,  
            user: videoInfo.user,  
            caption: videoInfo.caption,  
            outputPath: outputPath,  
            quality: this.options.quality,  
            size: fs.statSync(outputPath).size,  
            timestamp: new Date().toISOString()  
        };  
          
    } catch (error) {  
        console.error(`Error processing video: ${error.message}`);  
        return {  
            success: false,  
            error: error.message,  
            url: url  
        };  
    }  
}  
}
