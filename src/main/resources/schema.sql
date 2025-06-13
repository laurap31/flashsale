-- 設定預設編碼
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
    ) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO product (name, stock, price) VALUES
                                             ('iPhone 15', 50, 29999.00),
                                             ('Dyson 吸塵器', 30, 19900.00),
                                             ('PS5', 20, 14900.00);

-- 用戶表（密碼需用bcrypt，預設 testuser/test1234）
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
    ) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO user (username, password) VALUES
    ('testuser', '$2a$10$wH0xO4yI7jb5y2Il/HMTiOQ2k9n5tAbtUlFSnUu4CZ/npbq4zF.5u'); -- 密碼: test1234

-- 訂單表
CREATE TABLE IF NOT EXISTS flash_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
