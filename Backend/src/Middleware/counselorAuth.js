// middleware/counselorAuthMiddleware.js
import jwt from 'jsonwebtoken';
import Counselor from '../Models/counselorModel.js';

export const counselorAuthMiddleware = async (req, res, next) => {
  try {
    // Get token from request headers
    const token = req.headers.token || req.headers.authorization?.split(' ')[1];

    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Access denied. No token provided.',
      });
    }

    // Verify token
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Fetch counselor by ID
    const counselor = await Counselor.findById(decoded.id).select('-password');

    if (!counselor) {
      return res.status(401).json({
        success: false,
        message: 'Invalid token or counselor not found.',
      });
    }

    req.user = counselor; // attach counselor to req.user
    next();
  } catch (error) {
    console.error(error);
    res.status(401).json({
      success: false,
      message: 'Invalid token.',
    });
  }
};
