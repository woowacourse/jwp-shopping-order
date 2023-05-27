SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE product;
TRUNCATE TABLE member;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE cart;

INSERT INTO member (id, email, password) VALUES (1, 'a@a.com', '1234');
INSERT INTO member (id, email, password) VALUES (2, 'b@b.com', '1234');

INSERT INTO cart (id, member_id) values ('1', '1');
INSERT INTO cart (id, member_id) values ('2', '2');
