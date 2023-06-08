INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');
INSERT INTO product (name, price, image_url) VALUES ('팬케이크', 8000, 'https://images.unsplash.com/photo-1544726982-b414d58fabaa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVEJThDJUFDJUVDJUJDJTgwJUVDJTlEJUI0JUVEJTgxJUFDfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60');

INSERT INTO member (email, password, cash) VALUES ('dooly@dooly.com', '1234', 5000);
INSERT INTO member (email, password, cash) VALUES ('ber@ber.com', '1234', 5000);
INSERT INTO member (email, password, cash) VALUES ('bixx@bixx.com', '1234', 5000);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 4, 5);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 1, 5);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (3, 2, 5);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (3, 3, 10);

INSERT INTO cart_order (member_id, total_price) VALUES (1, 100000);
INSERT INTO cart_order (member_id, total_price) VALUES (1, 40000);
INSERT INTO cart_order (member_id, total_price) VALUES (2, 50000);
INSERT INTO cart_order (member_id, total_price) VALUES (3, 100000);

INSERT INTO order_item (cart_order_id, name, price, image_url, quantity, product_id) VALUES (1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 2, 1);
INSERT INTO order_item (cart_order_id, name, price, image_url, quantity, product_id) VALUES (1, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 4, 2);
INSERT INTO order_item (cart_order_id, name, price, image_url, quantity, product_id) VALUES (2, '팬케이크', 8000, 'https://images.unsplash.com/photo-1544726982-b414d58fabaa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVEJThDJUFDJUVDJUJDJTgwJUVDJTlEJUI0JUVEJTgxJUFDfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60', 5, 4);
INSERT INTO order_item (cart_order_id, name, price, image_url, quantity, product_id) VALUES (3, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 5, 1);
INSERT INTO order_item (cart_order_id, name, price, image_url, quantity, product_id) VALUES (4, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', 5, 2);


