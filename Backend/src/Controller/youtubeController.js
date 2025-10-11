import axios from "axios";
import dotenv from "dotenv";
import Chat from "../Models/ChatModel.js";
import { GoogleGenerativeAI } from "@google/generative-ai";

dotenv.config();

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);

export const search = async (req, res) => {
  try {
    const userId = req.user.id; // take from token

    if (!userId) {
      return res.status(400).json({ success: false, message: "userId is required" });
    }

    // 🧠 1️⃣ Fetch user's recent chats
    const chats = await Chat.find({ userId }).sort({ timestamp: -1 }).limit(100);
    if (!chats.length) {
      return res.status(404).json({ success: false, message: "No chat history found for user." });
    }

    const chatText = chats
      .map((c) => c.messages.map((m) => m.content).join(" "))
      .join(" ");

    // 🧠 2️⃣ Generate relevant tags using Gemini
    const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });
    const prompt = `
      Based on the user's recent chat, extract 3–5 short, lowercase tags 
      that represent the user's emotional or mental health topics.
      Return only a JSON array of tags.
                                         
      Chat:
      """${chatText}"""
    `;

    const tagResponse = await model.generateContent(prompt);

    let tags = [];
    try {
      tags = JSON.parse(tagResponse.response.text());
    } catch (err) {
      console.error("⚠️ Error parsing Gemini output:", tagResponse.response.text());
      tags = ["mental health"]; // fallback tag
    }

    console.log("🎯 Generated Tags:", tags);

    // 🎥 3️⃣ Fetch YouTube videos for each tag
    const apiKey = process.env.YOUTUBE_API_KEY;
    const youtubeApiUrl = "https://www.googleapis.com/youtube/v3/search";

    let allVideos = [];

    for (const tag of tags) {
      const ytResponse = await axios.get(youtubeApiUrl, {
        params: {
          part: "snippet",
          q: `${tag} mental health`,
          type: "video",
          maxResults: 5,
          key: apiKey,
        },
      });
              
      const videos = ytResponse.data.items.map((item) => ({
        tag,
        videoId: item.id.videoId,
        title: item.snippet.title,
        thumbnail: item.snippet.thumbnails.medium.url,
        channelTitle: item.snippet.channelTitle,
        url: `https://www.youtube.com/watch?v=${item.id.videoId}`,
      }));
    
      allVideos.push(...videos);
    }

    // 🧾 4️⃣ Respond with all tags + videos
    return res.json({
      success: true,
      data: {
        tags,
        videos: allVideos,
      },
    });
  } catch (error) {
    console.error("❌ Error in YouTube search:", error.response?.data || error.message);
    return res.status(500).json({
      success: false,
      message: "Error fetching YouTube videos.",
      error: error.message,
    });
  }
};
