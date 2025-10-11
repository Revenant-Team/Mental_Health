// utils/testEmbedding.js
import { pipeline } from '@xenova/transformers';

let embedder;

/**
 * Generate a 1024-dimension embedding using the LLaMA v2 embed model.
 */
export default async function getLocalEmbedding(text) {
  if (!embedder) {
    console.log("🔄 Loading local embedding model (llama-text-embed-v2)...");
  embedder = await pipeline('feature-extraction', 'Xenova/all-MiniLM-L6-v2');
    console.log("✅ Model loaded successfully (1024 dims).");
  }

  const output = await embedder(text, { pooling: 'mean', normalize: true });

  let data = output.data || output;
  if (Array.isArray(data[0])) {
    data = data.flat();
  }
            
  const embedding = Array.from(data);

  if (!embedding.length || embedding.some(v => typeof v !== 'number')) {
    throw new Error('Invalid embedding: not a numeric array');
  }

  return embedding;
}

