import express from 'express';
import { register, login, logout, getMe, updateProfile } from '../Controller/userController.js';
import { authMiddleware } from '../Middleware/auth.js';

const userRouter = express.Router();

userRouter.post('/register', register);
userRouter.post('/login', login);
userRouter.post('/logout', logout);
userRouter.get('/me', authMiddleware, getMe);
userRouter.put('/profile', authMiddleware, updateProfile);

export default userRouter;
