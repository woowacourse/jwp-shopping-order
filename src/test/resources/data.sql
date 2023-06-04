INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'testImage1');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'testImage2');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'testImage3');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

-- cart_item
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 3, 3);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 1, 3);

-- purchase_order
INSERT INTO purchase_order (member_id, order_at, payment, used_point, status) VALUES (1, '2023-05-20 12:12:12', 10000, 1000, 'Pending');
INSERT INTO purchase_order (member_id, order_at, payment, used_point, status) VALUES (1, '2023-05-25 14:13:12', 12000, 4000, 'Cancelled');
INSERT INTO purchase_order (member_id, order_at, payment, used_point, status) VALUES (1, '2023-05-31 17:11:12', 21000, 2000, 'Pending');

-- purchase_order_item
INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) VALUES (1, 1, '치킨', 10000, 'testImage1', 2);
INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) VALUES (1, 2, '샐러드', 20000, 'testImage2', 4);

INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) VALUES (2, 1, '치킨', 10000, 'testImage1', 5);
INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) VALUES (2, 3, '피자', 13000, 'testImage3', 3);

INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) VALUES (3, 3, '피자', 13000, 'testImage3', 5);
INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) VALUES (3, 2, '샐러드', 20000, 'testImage2', 7);

-- member_reward_point
INSERT INTO member_reward_point (member_id, point, created_at, expired_at, reward_order_id) VALUES (1, 500, '2023-05-20 12:12:12', '2023-05-25 12:12:12', 1);
INSERT INTO member_reward_point (member_id, point, created_at, expired_at, reward_order_id) VALUES (1, 700, '2023-05-18 12:12:12', '2023-05-29 12:12:12', 2);
INSERT INTO member_reward_point (member_id, point, created_at, expired_at, reward_order_id) VALUES (1, 1000, '2023-05-15 12:12:12', '2023-05-30 12:12:12', 3);

-- order_member_used_point
INSERT INTO order_member_used_point (order_id, used_reward_point_id, used_point) VALUES (1, 1, 500);
INSERT INTO order_member_used_point (order_id, used_reward_point_id, used_point) VALUES (1, 2, 400);
INSERT INTO order_member_used_point (order_id, used_reward_point_id, used_point) VALUES (1, 3, 800);
