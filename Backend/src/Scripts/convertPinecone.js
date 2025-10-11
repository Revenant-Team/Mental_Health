// utils/upsertPostsToPinecone.js
import { index } from '../Config/pinecone.js';
import Post from '../Models/postModel.js';
import getLocalEmbedding from './testEmbedding.js';

/**
 * Fetch active posts from MongoDB and upsert them to Pinecone.
 */
export async function upsertPostsToPinecone() {
  try {
    const posts = await Post.find({ 'metadata.isActive': true }).lean();

    console.log(`🗃️ Found ${posts.length} active posts to upsert.`);

    if (posts.length === 0) return;

    for (const post of posts) {
      try {
        const text = `${post.title || ''} ${post.content || ''} ${post.tags?.join(' ') || ''}`;
        const embedding = await getLocalEmbedding(text);

        if (!embedding || embedding.length === 0) {
          console.warn(`⚠️ No embedding generated for post ${post._id}`);
          continue;
        }

        const vector = {
          id: post._id.toString(),
          values: embedding,
          metadata: {
            title: post.title || '',
            category: post.category || '',
            tags: post.tags?.join(', ') || '',
            createdAt: post.metadata?.createdAt?.toString() || '',
          },
        };

        // Debug logs for each post
        console.log('--------------------------------------');
        console.log(`📄 Upserting Post ID: ${vector.id}`);
        console.log(`Embedding length: ${vector.values.length}`);
        console.log(`Embedding sample: ${vector.values.slice(0, 5)}`);
        console.log(`Metadata:`, vector.metadata);

        // ✅ Correct Pinecone v2 upsert syntax
        await index.upsert([
          {
            id: vector.id,
            values: vector.values,
            metadata: vector.metadata,
          },
        ]);

        console.log(`✅ Successfully upserted post ${post._id}\n`);
      } catch (err) {
        console.error(`❌ Error upserting post ${post._id}:`, err.message);
      }
    }

    console.log("🎯 All posts processed successfully.");
  } catch (error) {
    console.error("❌ Failed to upsert posts:", error.message);
  }
}
