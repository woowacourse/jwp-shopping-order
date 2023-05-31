DELETE FROM cart_item;
DELETE FROM member_coupon;
DELETE FROM order_coupon;
DELETE FROM order_product;
DELETE FROM coupon;
DELETE FROM product;
DELETE FROM `order`;
DELETE FROM member;

ALTER TABLE cart_item auto_increment = 1;
ALTER TABLE member_coupon auto_increment = 1;
ALTER TABLE order_coupon auto_increment = 1;
ALTER TABLE order_product auto_increment = 1;
ALTER TABLE coupon auto_increment = 1;
ALTER TABLE product auto_increment = 1;
ALTER TABLE member auto_increment = 1;
ALTER TABLE `order` auto_increment = 1;
