SET FOREIGN_KEY_CHECKS = 0;  -- 临时禁用外键检查

-- 删除现有表
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS Cart;
DROP TABLE IF EXISTS Inventory;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS PurchaseRequest;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Role;

-- 角色表
CREATE TABLE Role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 用户表
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE
);

-- 商品表
CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    image_url VARCHAR(255)
);

-- 进货申请表
CREATE TABLE PurchaseRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    requester_id INT NOT NULL,
    approver_id INT,
    request_date DATETIME NOT NULL,
    approval_date DATETIME,
    comment TEXT,
    FOREIGN KEY (product_id) REFERENCES Product(id),
    FOREIGN KEY (requester_id) REFERENCES User(id),
    FOREIGN KEY (approver_id) REFERENCES User(id)
);

-- 订单表
CREATE TABLE `Order` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    payment_method VARCHAR(50),
    total DECIMAL(10, 2),
    address_line VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    recipient_name VARCHAR(100),  -- 收货人姓名
    phone_number VARCHAR(20)      -- 电话号码
);

-- 订单详情表
CREATE TABLE order_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order`(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

-- 购物车表
CREATE TABLE Cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    product_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

-- 支付信息表
CREATE TABLE Payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES `Order`(id) ON DELETE CASCADE
);

-- 地址表
CREATE TABLE Address (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    recipient_name VARCHAR(100),  -- 收货人姓名
    phone_number VARCHAR(20),     -- 电话号码
    address_line VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT '中国',
    FOREIGN KEY (user_id) REFERENCES User(username) ON DELETE CASCADE
);

-- 库存表
CREATE TABLE Inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 1;  -- 重新启用外键检查