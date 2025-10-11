import express, { Router } from 'express'
import { chatting } from '../Controller/ChatController.js'
import { authMiddleware } from '../Middleware/auth.js'

const chatRouter = Router()

chatRouter.post('/chat_with_bot',authMiddleware,chatting)

export default chatRouter