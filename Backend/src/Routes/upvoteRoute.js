import express from 'express';
import { authMiddleware } from '../Middleware/auth.js';
import {
  togglePostUpvote,
  toggleReplyUpvote,
  getPostUpvotes,
  getReplyUpvotes
} from '../Controller/upvoteController.js';

const upvoteRouter = express.Router();

// Upvote/Downvote operations
upvoteRouter.post('/posts/:postId/upvote', authMiddleware, togglePostUpvote);  
upvoteRouter.post('/replies/:replyId/upvote', authMiddleware, toggleReplyUpvote); 
upvoteRouter.get('/posts/:postId/upvotes',authMiddleware, getPostUpvotes);                    
upvoteRouter.get('/replies/:replyId/upvotes',authMiddleware, getReplyUpvotes);               

export default upvoteRouter;
