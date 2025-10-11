import { pipeline } from '@xenova/transformers';

let embedder;

export default async function getLocalEmbedding(text) {
  if (!embedder) {
    embedder = await pipeline('feature-extraction', 'Xenova/all-MiniLM-L6-v2');
  }
  const output = await embedder(text, { pooling: 'mean', normalize: true });
  
  // output.data is usually a 2D array, flatten to 1D numeric array
  // If output.data is nested arrays, flatten first
  let flattened;
  if (Array.isArray(output.data[0])) {
    flattened = output.data.flat();
  } else {
    flattened = output.data;
  }

  // Convert to JS array if it is a Float32Array or ArrayBuffer
  return Array.from(flattened);
}
