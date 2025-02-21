CREATE TABLE users (
                      id INT AUTO_INCREMENT PRIMARY KEY, -- 用户ID
                      username VARCHAR(50) NOT NULL,    -- 用户名
                      password VARCHAR(50) NOT NULL    -- 密码
);

CREATE TABLE characters (
                              id INT AUTO_INCREMENT PRIMARY KEY,          -- 头像记录的唯一ID
                              user_id INT,                               -- 关联的用户ID（外键）
                              name VARCHAR(50) NOT NULL,                 -- name名称
                              biography TEXT,                            -- 个人简介
                              avatar_url VARCHAR(255),                   -- 头像的URL或路径
                              fans_count INT DEFAULT 0,                   -- 粉丝数量
                              follow_count INT DEFAULT 0,                 -- 关注数量
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE  -- 外键关联到用户表
);

CREATE TABLE posts (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT,              -- 发帖用户的ID（外键）
                       title VARCHAR(255),       -- 帖子标题
                       content TEXT,             -- 帖子内容
                       image_count INT DEFAULT 0, -- 帖子包含的图片数量（可选）
                       created_at TIMESTAMP,     -- 发布时间
                       updated_at TIMESTAMP,     -- 更新时间
                       is_deleted BOOLEAN DEFAULT 0, -- 是否删除标记
                       FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE comments (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          post_id INT,              -- 评论所属帖子ID（外键）
                          user_id INT,              -- 评论用户的ID
                          content TEXT,             -- 评论内容
                          created_at TIMESTAMP,     -- 评论时间
                          FOREIGN KEY (post_id) REFERENCES posts(id),
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 聊天会话表
CREATE TABLE chat_conversation (
                                   id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   user_id INT NOT NULL,
                                   title VARCHAR(255),
                                   create_time DATETIME NOT NULL,
                                   update_time DATETIME NOT NULL,
                                   FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 聊天消息表
CREATE TABLE chat_message (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id INT NOT NULL,
                              conversation_id BIGINT NOT NULL,
                              role VARCHAR(20) NOT NULL,
                              content TEXT NOT NULL,
                              create_time DATETIME NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (conversation_id) REFERENCES chat_conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;