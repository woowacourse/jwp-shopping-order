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
VALUES ('PET보틀-단지(480ml)', 41000,
        'https://cdn-mart.baemin.com/sellergoods/main/61d13e8f-63fe-4a19-baee-e84a2ae2b922.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-납작(260ml)', 61800,
        'https://cdn-mart.baemin.com/sellergoods/main/d07bec18-ce84-41c2-8903-61cbd10712b6.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-원형(500ml)', 70000,
        'https://cdn-mart.baemin.com/sellergoods/main/2ddb9f04-c15d-4647-b6e7-30afb9e8d072.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-원형(300ml)', 40400,
        'https://cdn-mart.baemin.com/sellergoods/main/09601088-36bc-484f-ba30-b6cb04eee0b8.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-삼각(330ml)', 65300,
        'https://cdn-mart.baemin.com/sellergoods/main/b51caccc-cd64-479a-a600-a7ce0507085f.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-삼각(530ml)', 10000,
        'https://cdn-mart.baemin.com/sellergoods/main/6e1e0dc3-4a10-4729-910a-ff3c837836fe.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-원형(600ml)', 47500,
        'https://cdn-mart.baemin.com/sellergoods/main/03e63566-5d56-4dc0-9357-2caaeaeebf7e.jpg?h=300&w=300');

INSERT INTO product (name, price, image_url)
VALUES ('PET보틀-납작(260ml)', 64200,
        'https://cdn-mart.baemin.com/sellergoods/main/d07bec18-ce84-41c2-8903-61cbd10712b6.jpg?h=300&w=300');

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