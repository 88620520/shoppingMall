-- 清空现有数据
DELETE FROM PurchaseRequest;
DELETE FROM OrderDetail;
DELETE FROM `Order`;
DELETE FROM Cart;
DELETE FROM Product;
DELETE FROM User;
DELETE FROM Role;

-- 插入角色数据
INSERT INTO Role (name) VALUES 
('CUSTOMER'),    -- 普通顾客
('STAFF'),       -- 店销职员
('MANAGER'),     -- 库存经理
('BOSS'),        -- 店铺Boss
('ADMIN')        -- 管理员
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入测试商品
INSERT INTO Product (name, description, price, stock, image_url) VALUES
('iPhone 15', '最新款苹果手机', 6999.00, 100, '/uploads/53abf1a6-e8bb-44d1-a4a9-8c5c94ff5a77.png'),
('MacBook Pro', '专业级笔记本电脑', 12999.00, 50, '/uploads/2c3907d3-7916-4f34-b128-f519e58cd1e9.jpg'),
('AirPods Pro', '无线降噪耳机', 1999.00, 200, '/uploads/27f55fcd-18ee-4827-a329-4f3f55f760eb.png');

-- 插入超级管理员账号 (密码是: admin123)
INSERT INTO User (username, password, email, role_id) 
SELECT 'admin', '$2a$10$bcRpwg4yci1OF6wre/BxY.3uu/Sgowq/uDcOf763QwvXH714SvVHu', 'admin@example.com', r.id
FROM Role r 
WHERE r.name = 'BOSS'
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 插入测试用的店员账号 (密码是: staff123)
INSERT INTO User (username, password, email, role_id)
SELECT 'staff', '$2a$10$4ONnNlTpTzEnwhNOra6jr.zI2OQ6xtim9YbngP11icqmBmZk6/rNG', 'staff@example.com', r.id
FROM Role r
WHERE r.name = 'STAFF'
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 插入测试用的库存经理账号 (密码是: manager123)
INSERT INTO User (username, password, email, role_id)
SELECT 'manager', '$2a$10$sZOwVCNr/sKMBnU9yNAzzu3M3acpy7KDfabAY28cqI8o9kxevD93G', 'manager@example.com', r.id
FROM Role r
WHERE r.name = 'MANAGER'
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 插入测试用的库存经理账号 (密码是: manager123)
INSERT INTO User (username, password, email, role_id)
SELECT 'test', '$2a$10$5YZvTexQjODOErpCAjaWHeYYXwAyJ7h4nAbormF7xUJx12d6sKH8O', 'test@example.com', r.id
FROM Role r
WHERE r.name = 'CUSTOMER'
ON DUPLICATE KEY UPDATE username = VALUES(username);