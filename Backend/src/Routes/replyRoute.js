import express from 'express';
import { authMiddleware } from '../Middleware/auth.js';
import {
  getPostReplies,
  replyToPost,
  replyToReply,
  updateReply,
  deleteReply,
  getReplyById
} from '../Controller/replyController.js';

const replyRouter = express.Router();

// Reply operations
replyRouter.get('/posts/:postId/replies', getPostReplies);           
replyRouter.post('/posts/:postId/replies', authMiddleware, replyToPost); 
replyRouter.post('/:replyId/replies', authMiddleware, replyToReply);  
replyRouter.get('/:replyId', getReplyById);                          
replyRouter.put('/:replyId', authMiddleware, updateReply);           
replyRouter.delete('/:replyId', authMiddleware, deleteReply);        

export default replyRouter;
