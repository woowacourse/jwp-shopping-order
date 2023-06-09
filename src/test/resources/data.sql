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

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (2, 3, 5);

INSERT INTO order_record (member_id, order_time)
VALUES (1, now());

INSERT INTO order_item (name, image_url, quantity, total_price, order_id)
VALUES ('치킨',
        'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80',
        2, 20000, 1);

INSERT INTO payment(order_id, original_total_price)
VALUES (1, 23500);

INSERT INTO applied_delivery_policy (payment_id, delivery_policy_id)
VALUES (1, 1);