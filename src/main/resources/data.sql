INSERT INTO product (name, price, image_url) VALUES ('바다',10000, 'https://img.lovepik.com/photo/20211119/large/lovepik-the-sea-picture_500143289.jpg');
INSERT INTO product (name, price, image_url) VALUES ('우아한형제들',90000, 'https://image.kmib.co.kr/online_image/2021/1217/2021121717103643262_1639728637_0016582097.jpg');
INSERT INTO product (name, price, image_url) VALUES ('네이버',100000, 'https://www.navercorp.com/img/ko/og/logo.png');
INSERT INTO product (name, price, image_url) VALUES ('카카오',80000, 'https://t1.kakaocdn.net/kakaocorp/corp_thumbnail/Kakao.png');
INSERT INTO product (name, price, image_url) VALUES ('라인',70000, 'https://vos.line-scdn.net/strapi-cluster-instance-bucket-84/brand_03_2e76928f15.jpeg');
INSERT INTO product (name, price, image_url) VALUES ('야놀자',60000, 'https://yanolja.in/static/share/share_thumb_1200x630.jpg');
INSERT INTO product (name, price, image_url) VALUES ('당근마켓',50000, 'https://img.sbs.co.kr/newimg/news/20201027/201484632.jpg');
INSERT INTO product (name, price, image_url) VALUES ('토스',90000, 'https://cdn.bizwatch.co.kr/news/photo/2022/09/05/b056d82a574f458946063dd8d2740334.jpg');
INSERT INTO product (name, price, image_url) VALUES ('두나무',40000, 'https://media.licdn.com/dms/image/C510BAQEcBzvlhAv6VQ/company-logo_200_200/0/1576729756780?e=2147483647&v=beta&t=UlFn7z0aP9AYZNrAW9Ktrdv1PRT9oTc3SWLnG_nBZS4');
INSERT INTO product (name, price, image_url) VALUES ('오늘의집',10000, 'https://s3-ap-northeast-1.amazonaws.com/bucketplace-v2-development/uploads/default_images/open_graph_icon_2.png');
INSERT INTO product (name, price, image_url) VALUES ('쿠팡',5000, 'https://blog.kakaocdn.net/dn/6ui8u/btqDtzRrCzg/ZM8q87L5frjEl0za6UnIk1/img.jpg');
INSERT INTO product (name, price, image_url) VALUES ('몰로코',3000, 'https://ditoday.com/wp-content/uploads/2021/02/digital_insight_moloco_logo.jpg');

INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('5000원 할인 쿠폰', 'reduction',10000,5000,0);
INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('10000 할인 쿠폰', 'reduction',10000,10000,0);
INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('50% 할인 쿠폰', 'percent',5000,0,0.5);
INSERT INTO coupon (`name`, discount_type,minimum_price, discount_price, discount_rate) VALUES ('20% 할인 쿠폰', 'percent',5000,0,0.2);
