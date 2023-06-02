INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');
INSERT INTO product (name, price, image_url) VALUES ('커피', 3000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000002081]_20210415133656839.jpg');
INSERT INTO product (name, price, image_url) VALUES ('피넛 쑥 스콘', 5000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2022/03/[9300000004028]_20220314152820975.jpg');
INSERT INTO product (name, price, image_url) VALUES ('거문 오름 크루아상', 4000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9300000001361]_20210421133918737.jpg');
INSERT INTO product (name, price, image_url) VALUES ('너티 크루아상', 6000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2023/01/[9300000004372]_20230102083042772.jpg');
INSERT INTO product (name, price, image_url) VALUES ('매콤 소시지 불고기 베이크', 11000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9300000002227]_20210421160744685.jpg');
INSERT INTO product (name, price, image_url) VALUES ('미니 리프 파이', 7000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2022/02/[9300000004008]_20220218143920309.jpg');
INSERT INTO product (name, price, image_url) VALUES ('스모크드 소시지 브레드', 8000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9300000002445]_20210421172107585.jpg');
INSERT INTO product (name, price, image_url) VALUES ('브리오슈 보스톡', 9000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/03/[9300000002931]_20210325161934333.jpg');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');
INSERT INTO member (email, password, points) VALUES ('rich@mail.com', '1234', 1000000);

INSERT INTO member (email, password, points) VALUES ('io@mail.com', '1234', 100000);
INSERT INTO member (email, password, points) VALUES ('zeeto@mail.com', '1234', 100000);
INSERT INTO member (email, password, points) VALUES ('ark@mail.com', '1234', 100000);
INSERT INTO member (email, password, points) VALUES ('hati@mail.com', '1234',100000);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (3, 1, 1);

-- order 20

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-1 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (1, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-2 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (2, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-3 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (3, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-4 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (4, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-5 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (5, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-6 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (6, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-7 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (7, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-8 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (8, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-9 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (9, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-10 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (10, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-11 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (11, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-12 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (12, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-13 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (13, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-14 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (14, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-15 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (15, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-16 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (16, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-17 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (17, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-18 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (18, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-19 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (19, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-20 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (20, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-21 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (21, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-22 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (22, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-23 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (23, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-24 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (24, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-25 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (25, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-26 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (26, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-27 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (27, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-28 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (28, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-29 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (29, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-30 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (30, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);

INSERT INTO orders (member_id, earned_points, used_points, total_price, pay_price, order_date) VALUES (3, 1000, 0, 10000, 10000, '2023-05-31 08:55:03');
INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (31, 1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1);
