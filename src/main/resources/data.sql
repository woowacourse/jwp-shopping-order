INSERT INTO product (name, price, image_url, point_ratio, point_available)
SELECT '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 10.0, true FROM DUAL
WHERE NOT EXISTS (SELECT * FROM product WHERE name = '치킨' AND price = 10000 AND image_url = 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80' AND point_ratio = 10.0 AND point_available = true);

INSERT INTO product (name, price, image_url, point_ratio, point_available)
SELECT '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 10.0, true FROM DUAL
WHERE NOT EXISTS (SELECT * FROM product WHERE name = '샐러드' AND price = 20000 AND image_url = 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80' AND point_ratio = 10.0 AND point_available = true);

INSERT INTO product (name, price, image_url, point_ratio, point_available)
SELECT '피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', 10.0, true FROM DUAL
WHERE NOT EXISTS (SELECT * FROM product WHERE name = '피자' AND price = 13000 AND image_url = 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80' AND point_ratio = 10.0 AND point_available = true);


INSERT INTO member (email, password, point)
SELECT 'a@a.com', '1234', 10000 FROM DUAL
WHERE NOT EXISTS (SELECT * FROM member WHERE email = 'a@a.com' AND password = '1234' AND point = 10000);

INSERT INTO member (email, password, point)
SELECT 'b@b.com', '1234', 1000 FROM DUAL
WHERE NOT EXISTS (SELECT * FROM member WHERE email = 'b@b.com' AND password = '1234' AND point = 1000);


INSERT INTO cart_item (member_id, product_id, quantity)
SELECT 1, 1, 2 FROM DUAL
WHERE NOT EXISTS (SELECT * FROM cart_item WHERE member_id = 1 AND product_id = 1 AND quantity = 2);

INSERT INTO cart_item (member_id, product_id, quantity)
SELECT 1, 2, 4 FROM DUAL
WHERE NOT EXISTS (SELECT * FROM cart_item WHERE member_id = 1 AND product_id = 2 AND quantity = 4);

INSERT INTO cart_item (member_id, product_id, quantity)
SELECT 2, 3, 5 FROM DUAL
WHERE NOT EXISTS (SELECT * FROM cart_item WHERE member_id = 2 AND product_id = 3 AND quantity = 5);
