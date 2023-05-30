INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'chickenImg');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'saladImg');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'pizzaImg');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);
