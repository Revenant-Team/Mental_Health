import {index} from '../Config/pinecone.js'
import Post from '../Models/postModel.js';
import getLocalEmbedding from './testEmbedding.js';

export async function upsertPostsToPinecone() {
  const posts = await Post.find({ 'metadata.isActive': true }).lean();

  for (const post of posts) {
    try {
      const text = `${post.title} ${post.content} ${post.tags?.join(' ') || ''}`;
      let embedding = await getLocalEmbedding(text);

      if (Array.isArray(embedding[0])) {
        embedding = embedding.flat();
      }

      if (!Array.isArray(embedding) || embedding.some(v => typeof v !== 'number')) {
        throw new Error('Embedding is not a flat array of numbers');
      }

      const vector = {
        id: post._id.toString(),
        values: embedding,
        metadata: {
          title: post.title,
          category: post.category,
          tags: post.tags,
          createdAt: post.metadata?.createdAt,
        },
      };

     console.log('Upsert argument vector ID:', vector.id);
console.log('Embedding length:', vector.values.length);
console.log('Embedding sample:', vector.values.slice(0, 5));
console.log('Metadata:', vector.metadata);


      await index.upsert({ vectors: [vector] });

      console.log(`Upserted post ${post._id} into Pinecone`);

    } catch (error) {
      console.error(`Error upserting post ${post._id}:`, error);
    }
  }
}


