INSERT INTO product (name, price, image_url) VALUES ('바다',10000, 'https://img.lovepik.com/photo/20211119/large/lovepik-the-sea-picture_500143289.jpg');
INSERT INTO product (name, price, image_url) VALUES ('우아한형제들',90000, 'https://image.kmib.co.kr/online_image/2021/1217/2021121717103643262_1639728637_0016582097.jpg');
INSERT INTO product (name, price, image_url) VALUES ('네이버',100000, 'https://www.navercorp.com/img/ko/og/logo.png');
INSERT INTO product (name, price, image_url) VALUES ('카카오',80000, 'https://t1.kakaocdn.net/kakaocorp/corp_thumbnail/Kakao.png');

INSERT INTO member (email, password) VALUES ('a@a', '123');
INSERT INTO member (email, password) VALUES ('b@b', '123');

INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('5000원 할인 쿠폰', 'deduction',10000,5000,0);
INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('50% 할인 쿠폰', 'percentage',5000,0,0.5);
INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('주문확정_1000원_할인_보너스쿠폰', 'deduction',5000,1000,0);
