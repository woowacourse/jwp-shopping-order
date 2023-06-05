SET FOREIGN_KEY_CHECKS = false;
TRUNCATE TABLE member;
TRUNCATE TABLE product;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE coupon;
TRUNCATE TABLE member_coupon;
TRUNCATE TABLE `order`;
TRUNCATE TABLE order_coupon;
TRUNCATE TABLE order_product;
SET FOREIGN_KEY_CHECKS = true;

INSERT INTO coupon (name, discount_rate, `period`, expired_at)
VALUES ('신규 가입 할인 쿠폰', 10, 7, '9999-12-31');

INSERT INTO coupon (name, discount_rate, `period`, expired_at)
VALUES ('테스트 쿠폰', 20, 7, '9999-12-31');
