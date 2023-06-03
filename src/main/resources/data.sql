
INSERT INTO product (name, price, image_url, is_discounted, discount_rate)
SELECT '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 0, 0
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = '치킨');

INSERT INTO product (name, price, image_url, is_discounted, discount_rate)
SELECT '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1, 50
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = '샐러드');

INSERT INTO product (name, price, image_url, is_discounted, discount_rate)
SELECT '피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', 1, 20
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = '피자');


INSERT INTO member (email, password, grade, total_price)
SELECT 'a@a.com', '1234', '일반', 0
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'a@a.com');

INSERT INTO member (email, password, grade, total_price)
SELECT 'b@b.com', '1234', 'gold', 0
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'b@b.com');


INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 3, 5);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 1, 1);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 2, 1);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 1);
