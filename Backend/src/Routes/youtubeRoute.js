import express from "express";
import { search } from "../Controller/youtubeController.js";
import { authMiddleware } from '../Middleware/auth.js';

const youtubeRouter = express();

youtubeRouter.post('/search',authMiddleware,search);

export default youtubeRouter;