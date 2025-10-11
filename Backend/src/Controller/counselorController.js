// controllers/counselorAuth.js
import Counselor from '../Models/counselorModel.js';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';

/**
 * Generate JWT token
 * @param {String} counselorId
 * @returns {String} JWT
 */
const generateToken = (counselorId) => {
  return jwt.sign({ id: counselorId }, process.env.JWT_SECRET);
};

/**
 * Register a new counselor
 */
export const registerCounselor = async (req, res) => {
  try {
    const { name, email, password, specialization, phone } = req.body;
    if (!name || !email || !password)
      return res.status(400).json({ message: 'All fields required' });

    const exists = await Counselor.findOne({ email });
    if (exists) return res.status(400).json({ message: 'Email already registered' });

    const hashedPassword = await bcrypt.hash(password, 10);

    const counselor = await Counselor.create({
      name,
      email,
      password: hashedPassword,
      specialization,
      phone,
    });

    const token = generateToken(counselor._id);

    res.status(201).json({
      success: true,
      data: {
        id: counselor._id,
        name: counselor.name,
        email: counselor.email,
        token,
      },
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ success: false, message: 'Server error' });
  }
};

/**
 * Login a counselor
 */
export const loginCounselor = async (req, res) => {
  try {
    const { email, password } = req.body;
    if (!email || !password)
      return res.status(400).json({ message: 'All fields required' });

    const counselor = await Counselor.findOne({ email });
    if (!counselor) return res.status(400).json({ message: 'Invalid credentials' });

    const isMatch = await bcrypt.compare(password, counselor.password);
    if (!isMatch) return res.status(400).json({ message: 'Invalid credentials' });

    const token = generateToken(counselor._id);

    res.json({
      success: true,
      data: {
        id: counselor._id,
        name: counselor.name,
        email: counselor.email,
        token,
      },
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ success: false, message: 'Server error' });
  }
};
