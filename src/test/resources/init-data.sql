set FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE product;
TRUNCATE TABLE member;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE coupon;

set FOREIGN_KEY_CHECKS = 1;

INSERT INTO product (id, name, price, image_url) VALUES (1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (id, name, price, image_url) VALUES (2, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (id, name, price, image_url) VALUES (3, '피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (id, email, password) VALUES (1, 'a@a.com', '1234');
INSERT INTO member (id, email, password) VALUES (2, 'b@b.com', '1234');

INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (1, 1, 1, 2);
INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (2, 1, 2, 4);
INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (3, 2, 3, 5);

INSERT INTO coupon (id, name, discount_type, discount_percent, discount_amount, minimum_price) VALUES (1, '1000원 할인 쿠폰', 'deduction', 0.0, 1000, 0);
