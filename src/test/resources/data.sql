DELETE FROM cart_item;
ALTER TABLE cart_item auto_increment = 1;
DELETE FROM order_item;
ALTER TABLE order_item auto_increment = 1;
DELETE FROM orders;
ALTER TABLE orders auto_increment = 1;
DELETE FROM product;
ALTER TABLE product auto_increment = 1;
DELETE FROM member;
ALTER TABLE member auto_increment = 1;

INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

-- member 1
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
-- member 2
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

-- 30000원 이하 주문
INSERT INTO orders (member_id, total_price, discount_price) VALUES (1, 23000, 0);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (1, 3, 1);
-- 30000원~50000원 주문
INSERT INTO orders (member_id, total_price, discount_price) VALUES (1, 30000, 2000);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (2, 1, 3);
-- 50000원 이상 주문
INSERT INTO orders (member_id, total_price, discount_price) VALUES (1, 63000, 5000);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (3, 1, 1);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (3, 2, 2);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (3, 3, 1);
