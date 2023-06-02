

INSERT INTO product (name, price, image_url, is_discounted, discount_rate) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 0, 0);
INSERT INTO product (name, price, image_url, is_discounted, discount_rate) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 1, 50);
INSERT INTO product (name, price, image_url, is_discounted, discount_rate) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', 1, 20);

INSERT INTO member (email, password, rank, total_price) VALUES ('a@a.com', '1234', '일반', 0);
INSERT INTO member (email, password, rank, total_price) VALUES ('b@b.com', '1234', 'gold', 0);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 3, 5);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 1, 1);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 2, 1);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 1);
