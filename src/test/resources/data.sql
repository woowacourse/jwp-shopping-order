INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

INSERT INTO coupon_type (name, description, discount_amount) VALUES ('1,000원 할인 쿠폰', '1,000원이 할인됩니다.', 1000) ;
INSERT INTO coupon_type (name, description, discount_amount) VALUES ('3,000원 할인 쿠폰', '3,000원이 할인됩니다.', 3000) ;
INSERT INTO coupon_type (name, description, discount_amount) VALUES ('5,000원 할인 쿠폰', '5,000원이 할인됩니다.', 5000) ;
INSERT INTO coupon_type (name, description, discount_amount) VALUES ('10,000원 할인 쿠폰', '10,000원이 할인됩니다.', 10000) ;

INSERT INTO coupon (usage_status, member_id, coupon_type_id) VALUES (1, 1, 1);
INSERT INTO coupon (usage_status, member_id, coupon_type_id) VALUES (0, 1, 2);
INSERT INTO coupon (usage_status, member_id, coupon_type_id) VALUES (0, 1, 3);
INSERT INTO coupon (usage_status, member_id, coupon_type_id) VALUES (0, 2, 1);
INSERT INTO coupon (usage_status, member_id, coupon_type_id) VALUES (1, 2, 4);

INSERT INTO orders (price, coupon_id, member_id) VALUES (45000, 3, 1);
INSERT INTO order_item (product_id, order_id, quantity) VALUES (1, 1, 2);

INSERT INTO order_item (product_id, order_id, quantity) VALUES (2, 1, 4);


