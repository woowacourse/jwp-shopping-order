INSERT INTO product (name, price, image_url) VALUES ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');
INSERT INTO product (name, price, image_url) VALUES ('햄버거', 8000, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVEJTk2JTg0JUVCJUIyJTg0JUVBJUIxJUIwfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60');
INSERT INTO product (name, price, image_url) VALUES ('제로 콜라', 2000, 'https://images.unsplash.com/photo-1630404365865-97ff92feba6a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTl8fCVFQyVCRCU5QyVFQiU5RCVCQ3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=800&q=60');
INSERT INTO product (name, price, image_url) VALUES ('감자튀김', 4000,'https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVBJUIwJTkwJUVDJTlFJTkwJUVEJThBJTgwJUVBJUI5JTgwfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60');
INSERT INTO product (name, price, image_url) VALUES ('오렌지', 3500,'https://images.unsplash.com/photo-1586788224331-947f68671cf1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzR8fCVFQSVCMyVCQyVFQyU5RCVCQ3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);
