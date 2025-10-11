// controllers/recommendPosts.js
import { index } from '../Config/pinecone.js';
import Chat from '../Models/ChatModel.js';
import { GoogleGenerativeAI } from '@google/generative-ai';
import getLocalEmbedding from '../Scripts/testEmbedding.js';

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);

export const recommendPosts = async (req, res) => {
  try {
    const userId = req.user.id;
    if (!userId) {
      return res.status(400).json({ success: false, message: 'userId is required' });
    }

    // 1️⃣ Fetch user's latest chat session
    const chatDoc = await Chat.findOne({ userId }).sort({ 'sessions.startedAt': -1 });

    if (!chatDoc || !chatDoc.sessions?.length) {
      return res.status(404).json({ success: false, message: 'No chat history found for user.' });
    }

    const recentSession = chatDoc.sessions.reduce((latest, session) => {
      return new Date(session.startedAt) > new Date(latest.startedAt) ? session : latest;
    }, chatDoc.sessions[0]);

    const chatText = recentSession.messages.map(m => m.content).join(' ');

    // 2️⃣ Generate tags using Gemini
    const model = genAI.getGenerativeModel({ model: 'gemini-2.5-flash' });
    const prompt = `
      Based on the user's recent chat, extract 3–5 short, lowercase tags 
      that represent the user's emotional or mental health topics.
      Return only a JSON array of tags (no extra text).

      Chat:
      """${chatText}"""
    `;

    const tagResponse = await model.generateContent(prompt);

    let tags = [];
    try {
      const raw = tagResponse.response.text().trim();
      const match = raw.match(/\[.*\]/s);
      tags = match ? JSON.parse(match[0]) : ['mental health'];
    } catch (err) {
      console.error('⚠️ Error parsing Gemini output:', tagResponse.response.text());
      tags = ['mental health'];
    }

    console.log('🎯 Generated Tags:', tags);

    // 3️⃣ Generate embedding for user's tags
    const tagEmbedding = await getLocalEmbedding(tags.join(' ')); // combine all tags

    // 4️⃣ Query Pinecone for similar posts
    const pineconeResult = await index.query({
      vector: tagEmbedding,
      topK: 100,
      includeMetadata: true,
    });

    // 5️⃣ Extract and sort posts by score descending
    const recommendedPosts = (pineconeResult.matches || [])
      .map(match => ({
        id: match.id,
        score: match.score,
        title: match.metadata.title,
        category: match.metadata.category,
        tags: match.metadata.tags,
        createdAt: match.metadata.createdAt,
      }))
      .sort((a, b) => b.score - a.score);

    // 6️⃣ Return tags + posts
    return res.json({
      success: true,
      data: {
        tags,
        recommendedPosts,
      },
    });
  } catch (error) {
    console.error('❌ Error recommending posts:', error.message);
    return res.status(500).json({
      success: false,
      message: 'Error recommending posts.',
      error: error.message,
    });
  }
};
