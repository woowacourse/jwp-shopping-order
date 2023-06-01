INSERT INTO product (name, price, image_url)
VALUES ('치킨', 10000,
        'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url)
VALUES ('샐러드', 20000,
        'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url)
VALUES ('피자', 13000,
        'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password)
VALUES ('a@a.com', '1234');
INSERT INTO member (email, password)
VALUES ('b@b.com', '1234');

INSERT INTO cart_item (quantity, product_id, name, product_price, image_url, member_id)
VALUES (2, 1, '치킨', 10000,
        'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80',
        1);

INSERT INTO cart_item (quantity, product_id, name, product_price, image_url, member_id)
VALUES (4, 2, '샐러드', 20000,
        'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80',
        1);

INSERT INTO cart_item (quantity, product_id, name, product_price, image_url, member_id)
VALUES (5, 3, '피자', 13000,
        'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80',
        2);

INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('회원가입 축하 쿠폰 🎉' 'RATE', 'ALL', null, 30, 1);
INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('회원가입 축하 쿠폰 🎉' 'RATE', 'ALL', null, 30, 2);

INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('생일 축하 쿠폰 🎂' 'FIX', 'SPECIFIC', 1, 3000, 1);
INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('어린이날 기념 쿠폰 🎁' 'RATE', 'SPECIFIC', 2, 10, 2);

INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('치킨 3000원 할인 쿠폰 🍗' 'FIX', 'SPECIFIC', 1, 3000, 1);
INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('치킨 3000원 할인 쿠폰 🍗' 'FIX', 'SPECIFIC', 1, 3000, 2);

INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('샐러드 50% 할인 쿠폰 🥗' 'RATE', 'SPECIFIC', 2, 50, 1);
INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('샐러드 50% 할인 쿠폰 🥗' 'RATE', 'SPECIFIC', 2, 50, 2);


INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('타이어보다 싸다. 전상품 80% 할인 쿠폰 🛞' 'RATE', 'ALL', null, 80, 1);
INSERT INTO coupon (name, discount_type, target_type, target_product_id, coupon_value, member_id)
VALUES ('타이어보다 싸다. 전상품 80% 할인 쿠폰 🛞' 'RATE', 'ALL', null, 80, 2);
