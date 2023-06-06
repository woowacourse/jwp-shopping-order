INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

-- 쿠폰 미리 등록하는 역할
INSERT INTO amount_discount (id, rate) VALUES (1, 10);
INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1);
INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS');
INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1);
INSERT INTO coupon_member(id, coupon_id, member_id) VALUES (1, 1, 1);
