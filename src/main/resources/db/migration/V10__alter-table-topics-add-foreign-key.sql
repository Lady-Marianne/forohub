-- Transformar user_id en clave foránea:
ALTER TABLE topics
ADD CONSTRAINT fk_topics_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;

