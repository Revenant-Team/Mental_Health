// models/Booking.js
import mongoose from 'mongoose';

const bookingSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  counselorId: { type: mongoose.Schema.Types.ObjectId, ref: 'Counselor', required: true },
  date: { type: Date, required: true }, // session date
  startTime: { type: String, required: true }, // "10:00", "11:00", etc.
  endTime: { type: String, required: true },   // "11:00", "12:00"
}, { timestamps: true });

bookingSchema.index({ counselorId: 1, date: 1, startTime: 1 }, { unique: true });

export default mongoose.model('Booking', bookingSchema);
