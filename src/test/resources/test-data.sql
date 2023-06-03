INSERT INTO member (id, `name`, email, password) VALUES (5L, '레오테스트', 'leotest@gmail.com', 'leo1234');

INSERT INTO product (id, `name`, price, image_url) VALUES (11L, '레오배변패드', 10000, '배변패드 URL');
INSERT INTO product (id, `name`, price, image_url) VALUES (10L, '비버꼬리', 5000, '비버꼬리 URL');
INSERT INTO product (id, `name`, price, image_url) VALUES (12L, '디노통구이', 3000, '통구이 URL');


INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (100L, 5L, 10L, 2);
INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (101L, 5L, 11L, 3);


INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (1L, 5L, 1L, 1);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (2L, 5L, 1L, 0);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (3L, 5L, 1L, 1);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (4L, 5L, 2L, 1);

INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount) VALUES (1L, '웰컴 쿠폰 - 10%할인', 10000, 10, 0);
INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount) VALUES (2L, '또 와요 쿠폰 - 3000원 할인', 15000, 0, 3000);


INSERT INTO orders (id, member_id, total_price, payment_price, point, created_at)
VALUES (100L, 5L, 30000, 3000, 0, '2023-06-03');


INSERT INTO ordered_item (id, order_id, product_name, product_price, product_quantity, product_image)
VALUES (100L, 100L, '레오배변패드', 30000, 1, 'image');
