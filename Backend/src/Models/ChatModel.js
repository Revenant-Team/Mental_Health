import mongoose from "mongoose";

// models/Chat.js


const MessageSchema = new mongoose.Schema({
  role: String, // "user" or "bot"
  content: String,
  timestamp: { type: Date, default: Date.now },
});

const ChatSchema = new mongoose.Schema({
  userId: String,
  conversation: [MessageSchema],
  createdAt: { type: Date, default: Date.now },
});

export default mongoose.model("Chat", ChatSchema);
