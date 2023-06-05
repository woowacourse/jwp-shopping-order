INSERT INTO member (id, `name`, email, password) VALUES (100, '레오', 'leo@gmail.com', 'leo123');
INSERT INTO member (id, `name`, email, password) VALUES (110, '디노', 'dino@gmail.com', 'dino123');
INSERT INTO member (id, `name`, email, password) VALUES (120, '비버', 'beaver@gmail.com', 'beaver123');
INSERT INTO member (id, `name`, email, password) VALUES (130, '가브리엘', 'gabriel@gmail.com', 'gabriel123');
INSERT INTO member (id, `name`, email, password) VALUES (140, '파인', 'fine@gmail.com', 'fine123');

INSERT INTO product (id, `name`, price, image_url) VALUES (110, '레오배변패드', 30000, 'https://img3.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202304/21/bemypet/20230421120037230todk.jpg');
INSERT INTO product (id, `name`, price, image_url) VALUES (100, '비버꼬리', 20000, 'https://i.namu.wiki/i/HOl5OVD4ve-7nEkNKWfwV6rynaZpN69taFAjlEODgVbf2EpRFQ0ZZo6l3kr63g7q8j4CCjwYkTvur3Y_sdDkTA.webp');
INSERT INTO product (id, `name`, price, image_url) VALUES (120, '디노통구이', 6000, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQneka04v3MDYEwObzw72kGOmQFjn_tRkkBEg&usqp=CAU');
INSERT INTO product (id, `name`, price, image_url) VALUES (130, '가브리엘빵', 2000, 'https://www.tlj.co.kr:7008/data/product/2021-12-29_event(5).JPG');
INSERT INTO product (id, `name`, price, image_url) VALUES (140, '파인장난감', 5000, 'https://miro.medium.com/v2/0*ZjYSm_q36J4KChdn');


INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (100, 100, 100, 2);
INSERT INTO cart_item (id, member_id, product_id, quantity) VALUES (101, 100, 110, 3);

INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount) VALUES (1, '웰컴 쿠폰 - 10%할인', 1000, 10, 0);
INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount) VALUES (2, '스페셜 쿠폰 - 20%할인', 5000, 20, 0);
INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount) VALUES (3, '또 와요 쿠폰 - 3000원 할인', 15000, 0, 3000);

INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (1, 100, 1, 1);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (2, 100, 1, 0);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (3, 100, 1, 1);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (4, 100, 2, 1);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (5, 100, 2, 1);
INSERT INTO member_coupon (id, member_id, coupon_id, status) VALUES (6, 100, 3, 1);

INSERT INTO orders (id, member_id, total_price, payment_price, point, created_at)
VALUES (1, 10, 30000, 3000, 0, '2023-06-03');


INSERT INTO ordered_item (id, order_id, product_name, product_price, product_quantity, product_image)
VALUES (1, 1, '레오배변패드', 30000, 1, 'image');

INSERT INTO point_history (id, member_id, order_id, used_point, earned_point) VALUES (1L, 10L, 1L, 10, 1000);


-- CREATE TABLE IF NOT EXISTS orders
-- (
--     id            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     member_id     BIGINT NOT NULL,
--     total_price   INT    NOT NULL,
--     payment_price INT    NOT NULL,
--     point         INT      DEFAULT 0,
--     created_at    DATETIME DEFAULT CURRENT_TIMESTAMP
-- );
--
--
-- CREATE TABLE IF NOT EXISTS ordered_item
-- (
--     id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     order_id         BIGINT       NOT NULL,
--     product_name     VARCHAR(255) NOT NULL,
--     product_price    INT          NOT NULL,
--     product_quantity INT          NOT NULL,
--     product_image    TEXT         NOT NULL
--     );
