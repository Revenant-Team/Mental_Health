import express from 'express';
import { registerCounselor,loginCounselor } from '../Controller/counselorController.js';
import { getAvailableSlots, bookSession, getMyBookings } from '../Controller/BookingController.js';
import { counselorAuthMiddleware } from '../Middleware/counselorAuth.js';
import { authMiddleware } from '../Middleware/auth.js';

const counselorRouter = express.Router();

// Counselor auth
counselorRouter.post('/register', registerCounselor);
counselorRouter.post('/login', loginCounselor);

// Booking routes (use your existing middleware)
counselorRouter.get('/bookings/available', counselorAuthMiddleware, getAvailableSlots);
counselorRouter.post('/bookings', authMiddleware, bookSession);
counselorRouter.get('/bookings/my', authMiddleware, getMyBookings);

export default counselorRouter;
