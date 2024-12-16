-- Renombrar la columna "id" en la tabla "users" a "user_id":
ALTER TABLE users
CHANGE COLUMN id user_id BIGINT NOT NULL AUTO_INCREMENT;

-- Renombrar la columna "id" en la tabla "topics" a "topic_id":
ALTER TABLE topics
CHANGE COLUMN id topic_id BIGINT NOT NULL AUTO_INCREMENT;
