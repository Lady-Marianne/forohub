ALTER TABLE topics MODIFY COLUMN status ENUM('ACTIVE', 'PENDING', 'CLOSED', 'ARCHIVED', 'DELETED') NOT NULL;