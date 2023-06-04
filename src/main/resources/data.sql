INSERT INTO product (name, image_url, price)
VALUES ('피자스쿨 치즈피자', 'https://t1.daumcdn.net/cfile/tistory/2647BE3754B7E8B733', 8900);
INSERT INTO product (name, image_url, price)
VALUES ('도미노 치즈피자', 'https://cdn.dominos.co.kr/admin/upload/goods/20200311_TI57KvOH.jpg', 23900);
INSERT INTO product (name, image_url, price)
VALUES ('서오릉 치즈피자', 'https://blog.kakaocdn.net/dn/bLtveJ/btqTJT1U7rX/sNzWENAmkZGyG5vOeknzzk/img.jpg', 20000);
INSERT INTO product (name, image_url, price)
VALUES ('도미노 페페로니피자', 'https://cdn.dominos.co.kr/admin/upload/goods/20200311_x8StB1t3.jpg', 25900);

INSERT INTO member (email, password)
VALUES ('pizza1@pizza.com', 'pizza');
INSERT INTO member (email, password)
VALUES ('pizza2@pizza.com', 'pizza');

INSERT INTO coupon (name, policy_type, discount_value, minimum_price, used, member_id)
VALUES ('10000원 이상 3000원 할인 쿠폰', 'PRICE', 3000, 10000, true, 1);
INSERT INTO coupon (name, policy_type, discount_value, minimum_price, used, member_id)
VALUES ('30000원 이상 4000원 할인 쿠폰', 'PRICE', 4000, 30000, false, 1);

INSERT INTO orders (delivery_fee, coupon_id, member_id)
values (3000, 1, 1);

INSERT INTO order_item (name, image_url, price, quantity, order_id)
values ('도미노 치즈피자', 'https://cdn.dominos.co.kr/admin/upload/goods/20200311_TI57KvOH.jpg', 23900, 1, 1);
