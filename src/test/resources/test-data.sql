INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

INSERT INTO coupon (name, discount_value, minimum_order_amount, end_date) VALUES ('1000원 할인 쿠폰', 1000, 0, '2023-12-31 23:59:59');
INSERT INTO coupon (name, discount_value, minimum_order_amount, end_date) VALUES ('2000원 할인 쿠폰', 2000, 0, '2023-12-31 23:59:59');
INSERT INTO coupon (name, discount_value, minimum_order_amount, end_date) VALUES ('3000원 할인 쿠폰', 3000, 0, '2023-12-31 23:59:59');

INSERT INTO member_coupon (member_id, coupon_id) VALUES (1, 1);
INSERT INTO member_coupon (member_id, coupon_id) VALUES (1, 3);
INSERT INTO member_coupon (member_id, coupon_id) VALUES (2, 1);
INSERT INTO member_coupon (member_id, coupon_id) VALUES (2, 2);