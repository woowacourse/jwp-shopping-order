INSERT INTO product (name, price, image_url)
VALUES ('지구별', 1000, 'https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('화성', 200000, 'https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('달', 300, 'https://cdn.pixabay.com/photo/2016/04/02/19/40/moon-1303512__480.png');
INSERT INTO product (name, price, image_url)
VALUES ('해왕성', 10000, 'https://cdn.pixabay.com/photo/2020/09/06/22/11/neptune-5550216__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('태양', 30000000, 'https://cdn.pixabay.com/photo/2016/07/13/20/47/sun-1515503__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('달에 가려진 태양', 8000000, 'https://cdn.pixabay.com/photo/2016/07/02/12/21/eclipse-1492818__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('금성', 9990, 'https://cdn.pixabay.com/photo/2011/12/13/14/39/venus-11022__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('코스모스 별', 15000, 'https://cdn.pixabay.com/photo/2018/03/08/20/01/astronomy-3209688__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('달을 낳는 지구', 6000000, 'https://cdn.pixabay.com/photo/2015/07/15/13/32/planet-846181__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('웜홀', 300000000, 'https://cdn.pixabay.com/photo/2020/06/17/09/28/wormhole-5308810__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('천왕성', 10000, 'https://cdn.pixabay.com/photo/2012/01/09/10/56/uranus-11625__480.jpg');
INSERT INTO product (name, price, image_url)
VALUES ('사건의 지평선', 700000000,
        'https://media.istockphoto.com/id/1299315343/ko/%EC%82%AC%EC%A7%84/%EB%B8%94%EB%9E%99%ED%99%80%EC%9D%98-%EC%A4%91%EB%A0%A5%EC%9E%A5-%EC%A4%91%EB%A0%A5%EC%9D%98-%EB%A7%A4%EB%A0%A5-%EC%9A%B4%EC%84%9D%EA%B3%BC-%EC%86%8C%ED%96%89%EC%84%B1%EC%9D%B4-%EC%82%BC%EC%BC%9C%EC%A7%88-%EB%AC%B4%EB%A0%B5.jpg?b=1&s=170667a&w=0&k=20&c=2fMcKJ-RNdzS1-v0NXjOVssmicbAH0twoCt_0AwCHOk=');

INSERT INTO member (email, nickname, password)
VALUES ('a@a.com', 'nickname1', '1234');
INSERT INTO member (email, nickname, password)
VALUES ('b@b.com', 'nickname2', '1234');

INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity)
VALUES (2, 3, 5);
