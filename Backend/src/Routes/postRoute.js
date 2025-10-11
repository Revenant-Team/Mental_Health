import express from 'express';
import { authMiddleware } from '../Middleware/auth.js';
import { getAllPosts,createPost,getMyPosts,getPostById,updatePost,deletePost,getTrendingTagsByMaxUpvotes } from '../Controller/postController.js';

import { recommendPosts } from '../Controller/recommendPostController.js'; 

const postRouter = express.Router();

// Post CRUD operations
postRouter.get('/', getAllPosts);                    
postRouter.post('/',authMiddleware, createPost);    
postRouter.get('/my-posts',authMiddleware, getMyPosts); 
postRouter.get('/:postId', getPostById);             
postRouter.put('/:postId',authMiddleware, updatePost);   
postRouter.delete('/:postId',authMiddleware, deletePost); 

postRouter.get('/trending/tags', getTrendingTagsByMaxUpvotes);

postRouter.post('/match-post',authMiddleware,recommendPosts);

export default postRouter;
