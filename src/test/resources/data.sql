SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE product_sale;
TRUNCATE TABLE order_item_history;
TRUNCATE TABLE coupon_history;
TRUNCATE TABLE order_table;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE cart;
TRUNCATE TABLE coupon;
TRUNCATE TABLE product;
TRUNCATE TABLE member;
TRUNCATE TABLE policy;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO member (id, email, password) VALUES (1, 'a@a.com', '1234');
INSERT INTO member (id, email, password) VALUES (2, 'b@b.com', '1234');

INSERT INTO cart (id, member_id) values ('1', '1');
INSERT INTO cart (id, member_id) values ('2', '2');
