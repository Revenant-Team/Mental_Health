import express from 'express';
import { createPost ,fetchPosts,getPost} from '../Controller/postController.js';
// import auth from '../Middleware/auth.js';

const postRouter = express.Router();


postRouter.post('/create', createPost);
postRouter.get('/posts', fetchPosts);
postRouter.get('/posts/:id', getPost);

export default postRouter;
