INSERT INTO PRODUCT (name, image_url, price)
VALUES ('피자스쿨 치즈피자', 'https://t1.daumcdn.net/cfile/tistory/2647BE3754B7E8B733', 8900);
INSERT INTO PRODUCT (name, image_url, price)
VALUES ('도미노 치즈피자', 'https://cdn.dominos.co.kr/admin/upload/goods/20200311_TI57KvOH.jpg', 23900);
INSERT INTO PRODUCT (name, image_url, price)
VALUES ('서오릉 치즈피자', 'https://blog.kakaocdn.net/dn/bLtveJ/btqTJT1U7rX/sNzWENAmkZGyG5vOeknzzk/img.jpg', 20000);
INSERT INTO PRODUCT (name, image_url, price)
VALUES ('도미노 페페로니피자', 'https://cdn.dominos.co.kr/admin/upload/goods/20200311_x8StB1t3.jpg', 25900);

INSERT INTO MEMBER (email, password)
VALUES ('pizza1@pizza.com', 'pizza');
INSERT INTO MEMBER (email, password)
VALUES ('pizza2@pizza.com', 'pizza');

INSERT INTO COUPON (name, policy_type, discount_price, discount_percent, discount_delivery_fee, condition_type,
                    minimum_price)
VALUES ('30000원 이상 3000원 할인 쿠폰', 'PRICE', 3000, 0, false, 'MINIMUM_PRICE', 3000);
INSERT INTO COUPON (name, policy_type, discount_price, discount_percent, discount_delivery_fee, condition_type,
                    minimum_price)
VALUES ('30000원 이상 4000원 할인 쿠폰', 'PRICE', 4000, 0, false, 'MINIMUM_PRICE', 3000);

INSERT INTO MEMBER_COUPON (coupon_id, member_id, used)
values (1, 1, false);
INSERT INTO MEMBER_COUPON (coupon_id, member_id, used)
values (2, 1, false);

