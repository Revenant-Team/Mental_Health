import express from 'express';
import { authMiddleware } from '../Middleware/auth.js';
import { getAllPosts,createPost,getMyPosts,getPostById,updatePost,deletePost } from '../Controller/postController.js';

const postRouter = express.Router();

// Post CRUD operations
postRouter.get('/', getAllPosts);                    
postRouter.post('/',authMiddleware, createPost);    
postRouter.get('/my-posts', getMyPosts); 
postRouter.get('/:postId', getPostById);             
postRouter.put('/:postId', updatePost);   
postRouter.delete('/:postId', deletePost); 

export default postRouter;
