import Chat from "../Models/ChatModel.js";
import axios from "axios";
import mongoose from "mongoose";

const STACK_API_URL = "https://api.stack-ai.com/inference/v0/run/451d41e9-d95f-4ebb-8371-6d10b86fab68/68d0ff331b22506911cab3a3";
const STACK_API_KEY = "b612108c-000a-4dc3-b5e0-67aac28479ce"; 

const chatting = async (req, res) => {
  const {userId,sessionId, userMessage } = req.body;
  // const userId = req.user.id
  // const {userId }= req.body
  if (!userId || !userMessage || !sessionId) {
    return res.status(400).json({ error: "userId, sessionId and userMessage are required" });
  }

  try {
    // 1️⃣ Send message to StackAI API
    const stackResponse = await axios.post(
      STACK_API_URL,
      { "in-0": userMessage },
      {
        headers: {
          Authorization: `Bearer ${STACK_API_KEY}`,
          "Content-Type": "application/json",
        },
      }
    );

    const botReply = stackResponse.data?.outputs?.["out-0"] || "No reply";

    // 2️⃣ Save to MongoDB under the session
    const chat = await Chat.findOne({ userId });

    const messagePair = [
      { role: "user", content: userMessage },
      { role: "bot", content: botReply }
    ];

    if (chat) {
      // Check if session exists
      const session = chat.sessions.find(s => s.sessionId === sessionId);
      if (session) {
        session.messages.push(...messagePair);
      } else {
        // Create new session
        chat.sessions.push({
          sessionId,
          startedAt: new Date(),
          messages: messagePair
        });
      }
      await chat.save();
    } else {
      // Create new user with session
      await Chat.create({
        userId,
        sessions: [
          {
            sessionId,
            startedAt: new Date(),
            messages: messagePair
          }
        ]
      });
    }

    // 3️⃣ Return bot response
    res.json({ reply: botReply });

  } catch (error) {
    console.error("Error chatting:", error.response?.data || error.message || error);
    res.status(500).json({ error: "Chat failed" });
  }
};

const startSession = async (userId) => {
  const sessionId = new mongoose.Types.ObjectId(); // unique session id
  await Chat.findOneAndUpdate(
    { userId },
    { $push: { sessions: { sessionId, startedAt: new Date(), messages: [] } } },
    { upsert: true }
  );
  return sessionId;
};


const addMessageToSession = async (userId, sessionId, role, content) => {
  await Chat.updateOne(
    { userId, "sessions.sessionId": sessionId },
    { $push: { "sessions.$.messages": { role, content } } }
  );
};



const endSession = async (userId, sessionId) => {
  await Chat.updateOne(
    { userId, "sessions.sessionId": sessionId },
    { $set: { "sessions.$.endedAt": new Date() } }
  );
};


export { chatting,startSession,endSession,addMessageToSession };
