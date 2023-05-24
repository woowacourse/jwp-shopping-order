INSERT INTO product (name, price, image_url) VALUES ('지구', 10000, 'https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg');
INSERT INTO product (name, price, image_url) VALUES ('화성', 200000, 'https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg');
INSERT INTO product (name, price, image_url) VALUES ('달', 300, 'https://cdn.pixabay.com/photo/2016/04/02/19/40/moon-1303512__480.png');
INSERT INTO product (name, price, image_url) VALUES ('해왕성', 10000, 'https://cdn.pixabay.com/photo/2020/09/06/22/11/neptune-5550216__480.jpg');
INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);
