set referential_integrity false;
truncate table product restart identity;
truncate table member restart identity;
truncate table cart_item restart identity;
truncate table orders restart identity;
truncate table orders_product restart identity;
truncate table orders_coupon restart identity;
set referential_integrity true;

INSERT INTO product (name, price, image_url) VALUES ('제네시스 g80', 1000000, 'https://www.genesis.com/content/dam/genesis-p2/kr/assets/utility/sns/genesis-kr-model-g80-02-social-1200x630-ko.jpg');
INSERT INTO product (name, price, image_url) VALUES ('부가티 시론', 2000000, 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Bugatti_Chiron_%2836559710091%29.jpg/300px-Bugatti_Chiron_%2836559710091%29.jpg');
INSERT INTO product (name, price, image_url) VALUES ('롤스로이스 펜텀', 13000, 'https://www.motoya.co.kr/news/photo/202205/35313_220048_1952.jpg');
INSERT INTO product (name, price, image_url) VALUES ('현루피', 10000, 'https://wepick.kr/wp-content/uploads/2022/08/image-21.png');
INSERT INTO product (name, price, image_url) VALUES ('맥북', 1500000, 'https://thumbnail8.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/398911002531188-97af85fd-bfc2-4561-b086-a8ef7db72b72.jpg');
INSERT INTO product (name, price, image_url) VALUES ('아이폰 14', 13000, 'https://thumbnail7.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/2022/09/21/15/6/d39d5f95-018e-421e-b951-00028fe9eefe.jpg');
INSERT INTO product (name, price, image_url) VALUES ('우아한테크코스', 500000, 'https://woowacourse.github.io/logo_thumnail_bg.jpg');
INSERT INTO product (name, price, image_url) VALUES ('애플워치', 20000, 'https://gdimg.gmarket.co.kr/2506520142/still/280');
INSERT INTO product (name, price, image_url) VALUES ('잠실 주공아파트', 25000000, 'https://i.namu.wiki/i/9StoLdtm2LwjViBvwhHOBtswiwERoXDXDjdQD8WY0vUoatMSd3vcdv4EQ_9xsc3ylOm4lPVdS7LxmyKtZByFYA.webp');
INSERT INTO product (name, price, image_url) VALUES ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80');
INSERT INTO product (name, price, image_url) VALUES ('한남더힐', 30000000, 'https://newsimg.hankookilbo.com/cms/articlerelease/2020/10/16/6a5280a0-41ba-4ff6-b614-0a137a429244.jpg');
INSERT INTO product (name, price, image_url) VALUES ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 3, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);

INSERT INTO coupon (name,discount_type,discount_rate,discount_amount,minimum_price) VALUES ('10% 할인', 'percentage',0.1,0,0);
