DELETE FROM cart_item;
DELETE FROM member;
DELETE FROM product;

ALTER TABLE cart_item auto_increment = 1;
ALTER TABLE member auto_increment = 1;
ALTER TABLE product auto_increment = 1;
