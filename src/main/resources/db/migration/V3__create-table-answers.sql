-- Crear la tabla 'answers':

CREATE TABLE answers (
    answer_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    message VARCHAR(2000) NOT NULL,
    topic VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    author VARCHAR(100),
    solution BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_topic FOREIGN KEY (topic_id) REFERENCES topics(topic_id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);