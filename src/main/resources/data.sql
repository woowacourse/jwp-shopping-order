INSERT INTO policy (id, isPercentage, amount) VALUES (1, true, 0);
INSERT INTO policy (id, isPercentage, amount) VALUES (2, true, 0);
INSERT INTO policy (id, isPercentage, amount) VALUES (3, true, 0);
INSERT INTO policy (id, isPercentage, amount) VALUES (4, true, 0);

INSERT INTO policy (id, isPercentage, amount) VALUES (5, true, 10);
INSERT INTO policy (id, isPercentage, amount) VALUES (6, false, 2000);
INSERT INTO policy (id, isPercentage, amount) VALUES (7, false, 3000);

INSERT INTO product (id, name, price, image_url, isOnSale, salePrice) VALUES (1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', false, 0);
INSERT INTO product (id, name, price, image_url , isOnSale, salePrice) VALUES (2, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80', false, 0);
INSERT INTO product (id, name, price, image_url , isOnSale, salePrice) VALUES (3, '피자', 20000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', false, 0);
INSERT INTO product (id, name, price, image_url , isOnSale, salePrice) VALUES (4, '보쌈', 20000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80', false, 0);

INSERT INTO member (id, email, password) VALUES (1, 'a@a.com', '1234');
INSERT INTO member (id, email, password) VALUES (2, 'b@b.com', '1234');

INSERT INTO coupon (id, name, policy_id, member_id) VALUES (1, '전체 10% 할인 쿠폰', 5, 1);
INSERT INTO coupon (id, name, policy_id, member_id) VALUES (2, '전체 2000원 할인 쿠폰', 6, 1);
INSERT INTO coupon (id, name, policy_id, member_id) VALUES (3, 'DELIVERY_FREE', 7, 1);
INSERT INTO coupon (id, name, policy_id, member_id) VALUES (4, 'DELIVERY_FREE', 7, 2);

INSERT INTO cart (id, member_id) values ('1', '1');
INSERT INTO cart (id, member_id) values ('2', '2');

INSERT INTO cart_item (id, cart_id, product_id, quantity) VALUES (1, 1, 1, 10);
INSERT INTO cart_item (id, cart_id, product_id, quantity) VALUES (2, 1, 2, 10);
INSERT INTO cart_item (id, cart_id, product_id, quantity) VALUES (3, 1, 3, 10);
