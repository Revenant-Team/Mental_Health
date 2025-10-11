
import axios from 'axios';

export const search = async(req,res)=>{
const { tag } = req.body; 
  if (!tag) {
    return res.status(400).json({ success: false, message: 'Tag is required' });
  }

  try {
    const apiKey = process.env.YOUTUBE_API_KEY; 
    const youtubeApiUrl = `https://www.googleapis.com/youtube/v3/search`;

    const response = await axios.get(youtubeApiUrl, {
      params: {
        part: 'snippet',
        q: tag,
        type: 'video',
        maxResults: 10,
        key: apiKey,
      }
    });

    const videos = response.data.items.map(item => ({
      videoId: item.id.videoId,
      title: item.snippet.title,
      description: item.snippet.description,
      thumbnail: item.snippet.thumbnails.medium.url,
      channelTitle: item.snippet.channelTitle,
      publishedAt: item.snippet.publishedAt,
      url: `https://www.youtube.com/watch?v=${item.id.videoId}`
    }));

    return res.json({ success: true, data: { videos } });
  } catch (error) {
    console.error("YouTube API Error:", error.response?.data || error.message);
    return res.status(500).json({ success: false, message: 'Error fetching YouTube videos.' });
  }
};

