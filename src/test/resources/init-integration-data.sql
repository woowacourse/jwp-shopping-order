DELETE
FROM orders_product;
DELETE
FROM orders;
DELETE
FROM cart_item;
DELETE
FROM member_coupon;
DELETE
FROM product;
DELETE
FROM member;
DELETE
FROM coupon;

INSERT INTO member (id, email, password)
VALUES (1, 'a@a.com', '1234');
INSERT INTO member (id, email, password)
VALUES (2, 'b@b.com', '1234');
INSERT INTO member (id, email, password)
VALUES (3, 'sangun', '1234');
INSERT INTO member (id, email, password)
VALUES (4, 'lopi', '1234');
