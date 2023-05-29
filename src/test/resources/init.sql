DELETE FROM cart_item;
DELETE FROM member;
DELETE FROM product;
DELETE FROM coupon;

ALTER TABLE cart_item auto_increment = 1;
ALTER TABLE member auto_increment = 1;
ALTER TABLE product auto_increment = 1;
ALTER TABLE coupon auto_increment = 1;
