INSERT INTO product (name, price, image_url, point_ratio, point_available) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 0.0, false);
INSERT INTO product (name, price, image_url, point_ratio, point_available) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 0.0, false);
INSERT INTO product (name, price, image_url, point_ratio, point_available) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', 0.0, false);

INSERT INTO member (email, password, point) VALUES ('a@a.com', '1234', 0);
INSERT INTO member (email, password, point) VALUES ('b@b.com', '1234', 0);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

INSERT INTO `order` (member_id, original_price, used_point, point_to_add) VALUES (1, 1000, 100, 5);
INSERT INTO `order` (member_id, original_price, used_point, point_to_add) VALUES (2, 2000, 200, 50);

INSERT INTO `order_info` (order_id, product_id, name, price, image_url, quantity) VALUES (1, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 2);
INSERT INTO `order_info` (order_id, product_id, name, price, image_url, quantity) VALUES (2, 1, '샐러드', 20000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', 3);
