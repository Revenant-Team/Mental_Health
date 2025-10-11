import crypto from 'crypto';

export const generateAnonymousId = (userId) => {
  const salt = process.env.USER_ANONYMOUS_SALT;
  const data = `${userId}-${salt}`;
  return crypto.createHash('sha256').update(data).digest('hex').substring(0, 12);
};

export const generatePostHash = (userId, postData) => {
  const salt = process.env.ANONYMOUS_SALT;
  const data = `${userId}-${postData.title}-${Date.now()}-${salt}`;
  return crypto.createHash('sha256').update(data).digest('hex').substring(0, 16);
};
