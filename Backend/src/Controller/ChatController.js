import Chat from "../Models/ChatModel.js";
import axios from "axios";
const STACK_API_URL = "https://api.stack-ai.com/inference/v0/run/451d41e9-d95f-4ebb-8371-6d10b86fab68/68d0ff331b22506911cab3a3";
const STACK_API_KEY = "b612108c-000a-4dc3-b5e0-67aac28479ce"; 


const chatting = async (req, res) => {
  const { userId, userMessage } = req.body;

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
    // console.log(stackResponse.data);
    // 2️⃣ Save to MongoDB
    const chat = await Chat.findOne({ userId });

    if (chat) {
      chat.conversation.push({ role: "user", content: userMessage });
      chat.conversation.push({ role: "bot", content: botReply });
      await chat.save();
    } else {
      await Chat.create({
        userId,
        conversation: [
          { role: "user", content: userMessage },
          { role: "bot", content: botReply },
        ],
      });
    }

    // 3️⃣ Return bot response
    res.json({ reply: botReply });
  } catch (error) {
      console.error("Error chatting:", error.response?.data || error.message || error);
    res.status(500).json({ error: "Chat failed" });
  }
}

export {chatting}