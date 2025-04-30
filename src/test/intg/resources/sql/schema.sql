
-- ========================
-- 📚 BOOK TABLE
-- ========================
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn CHAR(13) NOT NULL,
    title VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255),
    summary TEXT,
    price INT,
    published_at DATETIME
);

-- ========================
-- 🧑 MEMBERS TABLE
-- ========================
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(15) NOT NULL,
    created_at DATETIME,
    modified_at DATETIME
);

-- ========================
-- 📂 CATEGORIES TABLE
-- ========================
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- ========================
-- 📝 POSTS TABLE
-- ========================
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('AVAILABLE', 'RESERVED', 'SOLDOUT'),
    category_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    content TEXT,
    price INT,
    view_count INT DEFAULT 0,
    created_at DATETIME,
    modified_at DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- ========================
-- ⭐ LIKE_POSTS TABLE
-- ========================
CREATE TABLE like_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    liked_at DATETIME,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

-- ========================
-- 📩 NOTIFICATIONS TABLE
-- ========================
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_id BIGINT NOT NULL,
    message VARCHAR(255) NOT NULL,
    status BOOLEAN DEFAULT FALSE,
    type ENUM('CHAT', 'SYSTEM', 'ETC'),
    created_at DATETIME,
    FOREIGN KEY (receiver_id) REFERENCES members(id)
);

-- ========================
-- 💬 CHAT_ROOMS TABLE
-- ========================
CREATE TABLE chat_rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (sender_id) REFERENCES members(id),
    FOREIGN KEY (receiver_id) REFERENCES members(id)
);

-- ========================
-- 💬 CHAT_MESSAGES TABLE
-- ========================
CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    status BOOLEAN DEFAULT FALSE,
    created_at DATETIME,
    FOREIGN KEY (chat_room_id) REFERENCES chat_rooms(id),
    FOREIGN KEY (sender_id) REFERENCES members(id)
);
