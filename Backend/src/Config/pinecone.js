// Config/pinecone.js
import { Pinecone } from '@pinecone-database/pinecone';
import "dotenv/config";

const pc = new Pinecone({
  apiKey: process.env.PINECONE_API_KEY,
});

// ⚠️ Ensure the index name exists in Pinecone dashboard and its dimension = 384
const index = pc.index('post');

export { pc, index };
