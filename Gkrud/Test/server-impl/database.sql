-- USERS 테이블
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100),
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- POSTS 테이블
CREATE TABLE posts (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       title VARCHAR(200) NOT NULL,
                       content TEXT NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 페이징용: 최신순 10개
-- SELECT * FROM posts ORDER BY created_at DESC LIMIT #{offset}, 10;

-- 상세조회
-- SELECT p.*, u.username FROM posts p
-- JOIN users u ON p.user_id = u.id
-- WHERE p.id = #{id};

-- 등록 / 수정 / 삭제 쿼리도 각각 INSERT / UPDATE / DELETE 로 작성
