INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-정사각(420ml)', 43400,
        'https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-밀크티(370ml)', 73400,
        'https://cdn-mart.baemin.com/sellergoods/main/ac90cb6d-70ad-4271-a25e-03e4db9a9960.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-정사각(370ml)', 41000,
        'https://cdn-mart.baemin.com/sellergoods/main/fbe1660a-20f4-4077-8ce7-d8926c7b4e6d.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-납작(450ml)', 39900,
        'https://cdn-mart.baemin.com/sellergoods/main/6adcd3f3-25a3-4038-82a4-322eb72ec281.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-단지(480ml)', 39900,
        'https://cdn-mart.baemin.com/sellergoods/main/61d13e8f-63fe-4a19-baee-e84a2ae2b922.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-납작(260ml)', 41000,
        'https://cdn-mart.baemin.com/sellergoods/main/d07bec18-ce84-41c2-8903-61cbd10712b6.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-딸기(500ml)', 61800,
        'https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-원형(500ml)', 30500,
        'https://cdn-mart.baemin.com/sellergoods/main/09601088-36bc-484f-ba30-b6cb04eee0b8.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-스몰(500ml)', 50000,
        'https://cdn-mart.baemin.com/sellergoods/main/b51caccc-cd64-479a-a600-a7ce0507085f.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-동글(400ml)', 44500,
        'https://cdn-mart.baemin.com/sellergoods/main/6e1e0dc3-4a10-4729-910a-ff3c837836fe.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-딸기라떼(500ml)', 18000,
        'https://cdn-mart.baemin.com/sellergoods/main/03e63566-5d56-4dc0-9357-2caaeaeebf7e.jpg?h=300&w=300');
INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-힙 플라스크(500ml)', 21500,
        'https://cdn-mart.baemin.com/sellergoods/main/d07bec18-ce84-41c2-8903-61cbd10712b6.jpg?h=300&w=300');

INSERT INTO member (email, password)
VALUES ('a@a.com', '1234');
INSERT INTO member (email, password)
VALUES ('b@b.com', '1234');
INSERT INTO member (email, password)
VALUES ('c@c.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 3, 5);

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 4, 3);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 5, 2);

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 6, 2);

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (2, 6, 2);



INSERT INTO coupon (name, discount_type, discount)
VALUES ('배송비 할인', 'FIXED_AMOUNT', 3000);

INSERT INTO coupon (name, discount_type, discount)
VALUES ('신규 가입 할인', 'FIXED_AMOUNT', 5000);

INSERT INTO coupon (name, discount_type, discount)
VALUES ('여름 맞이 할인', 'FIXED_AMOUNT', 2000);

INSERT INTO coupon (name, discount_type, discount)
VALUES ('테스트 코드 할인', 'FIXED_AMOUNT', 1000000);


INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (1, 'DF-001', true);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (1, 'DF-002', true);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (1, 'DF-003', false);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (2, 'SU-001', false);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (2, 'SU-002', true);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (2, 'SU-003', true);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (3, 'SE-001', false);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (3, 'SE-002', false);

INSERT INTO coupon_serial_number (coupon_id, serial_number, is_issued)
VALUES (3, 'SE-003', false);



INSERT INTO member_coupon (member_id, coupon_serial_number_id)
VALUES (1, 1);

INSERT INTO member_coupon (member_id, coupon_serial_number_id)
VALUES (2, 2);
INSERT INTO member_coupon (member_id, coupon_serial_number_id)
VALUES (2, 5);

INSERT INTO member_coupon (member_id, coupon_serial_number_id)
VALUES (3, 6);

INSERT INTO orders (member_id, coupon_id, delivery_fee, order_status)
VALUES (1, 0, 3000, 'PAID');
INSERT INTO orders (member_id, coupon_id, delivery_fee, order_status)
VALUES (1, 0, 5000, 'CANCELED');
INSERT INTO orders (member_id, coupon_id, delivery_fee, order_status)
VALUES (1, 2, 5000, 'PAID');

INSERT INTO orders (member_id, coupon_id, delivery_fee, order_status)
VALUES (2, 0, 4000, 'PAID');

-- 1번 주문

-- 1번 product
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (1, 'PET보틀-정사각(420ml)', 43400,
        'https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300',
        2);

-- 2번 product
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (1, 'PET보틀-밀크티(370ml)', 73400,
        'https://cdn-mart.baemin.com/sellergoods/main/ac90cb6d-70ad-4271-a25e-03e4db9a9960.jpg?h=300&w=300',
        4);

-- 3번 product
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (1, 'PET보틀-정사각(370ml)', 41000,
        'https://cdn-mart.baemin.com/sellergoods/main/fbe1660a-20f4-4077-8ce7-d8926c7b4e6d.jpg?h=300&w=300',
        5);

-- 2번 주문

-- 4번 product
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (2, 'PET보틀-납작(450ml)', 39900,
        'https://cdn-mart.baemin.com/sellergoods/main/6adcd3f3-25a3-4038-82a4-322eb72ec281.jpg?h=300&w=300',
        3);

-- 5번 product
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (2, 'PET보틀-단지(480ml)', 39900,
        'https://cdn-mart.baemin.com/sellergoods/main/61d13e8f-63fe-4a19-baee-e84a2ae2b922.jpg?h=300&w=300',
        2);

-- 3번 주문

-- 6번 product
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (3, 'PET보틀-납작(260ml)', 41000,
        'https://cdn-mart.baemin.com/sellergoods/main/d07bec18-ce84-41c2-8903-61cbd10712b6.jpg?h=300&w=300',
        2);

-- 4번 주문
INSERT INTO order_item (order_id, name, price, image_url, quantity)
VALUES (4, 'PET보틀-납작(260ml)', 41000,
        'https://cdn-mart.baemin.com/sellergoods/main/d07bec18-ce84-41c2-8903-61cbd10712b6.jpg?h=300&w=300',
        3);
