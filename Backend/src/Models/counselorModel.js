// models/Counselor.js
import mongoose from 'mongoose';

const counselorSchema = new mongoose.Schema({
  name: { type: String, required: true },
  specialization: { type: String },
  email: { type: String, unique: true, required: true },
  phone: { type: String },
  password: { type: String, required: true }, // new
  role: { type: String, default: 'counselor' }, // optional
}, { timestamps: true });

export default mongoose.model('Counselor', counselorSchema);
