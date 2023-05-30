INSERT INTO member (email, password) VALUES ('a@a.com', '1234');
INSERT INTO member (email, password) VALUES ('b@b.com', '1234');

INSERT INTO product (name, price, image_url, stock) VALUES ('담은 막걸리', 11000, 'https://github.com/hae-on/woowacourse/assets/80464961/72003c22-179e-4135-8a9e-ef2c98e79158', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('고도리 복숭아 와인', 20000, 'https://assets.business.veluga.kr/media/public/%E1%84%80%E1%85%A9%E1%84%83%E1%85%A9%E1%84%85%E1%85%B5_%E1%84%87%E1%85%A9%E1%86%A8%E1%84%89%E1%85%AE%E1%86%BC%E1%84%8B%E1%85%A1_%E1%84%8B%E1%85%AA%E1%84%8B%E1%85%B5%E1%86%AB_hO7dpLV.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('도원결의', 6000, 'https://assets.business.veluga.kr/media/public/%E1%84%83%E1%85%A9%E1%84%8B%E1%85%AF%E1%86%AB%E1%84%80%E1%85%A7%E1%86%AF%E1%84%8B%E1%85%B4%E1%84%89%E1%85%AE%E1%86%AB_1_MBSOkiY.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('티나', 7800, 'https://github.com/inyeong-kang/react-master-class/assets/81199414/f513b684-4ab7-4ba7-b7bb-c35b8932b147', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('다래 와인 7004', 15000, 'https://assets.business.veluga.kr/media/public/%E1%84%83%E1%85%A1%E1%84%85%E1%85%A2%E1%84%8B%E1%85%AA%E1%84%8B%E1%85%B5%E1%86%AB_7004S.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('원매', 20000, 'https://assets.business.veluga.kr/media/public/%E1%84%8B%E1%85%AF%E1%86%AB%E1%84%86%E1%85%A2_15.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('스윗 마마', 6000, 'https://cdn.veluga.kr/files/supplier/46/drinks/2880_Sweet_Mama.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('롱 아일랜드 아이스티', 12000, 'https://github.com/inyeong-kang/react-master-class/assets/81199414/eb08cc0e-899e-48a9-beb3-bbd0835fc0e1', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('피즈 애플', 2500, 'https://assets.business.veluga.kr/media/public/Cesu_Alus_FIZZ_Apple.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('에반 윌리엄스 블랙', 30900, 'https://cdn.veluga.kr/drinks/0/main/3e65f43347dd46c3ab2f1abc25cf99f4_%EC%97%90%EB%B0%98_%EC%9C%8C%EB%A6%AC%EC%97%84%EC%8A%A4_%EB%B8%94%EB%9E%99.png', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('블루 하와이', 13000, 'https://github.com/inyeong-kang/react-master-class/assets/81199414/ee2879e1-a56d-48a1-9815-4000d5f0bd65', 100);
INSERT INTO product (name, price, image_url, stock) VALUES ('토버모리', 122900, 'https://cdn.veluga.kr/files/supplier/71/drinks/25148_Main_-_Tobermory_12yo.png', 100);

INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 2);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 2, 4);
INSERT INTO cart_item (member_id, product_id, quantity) VALUES (2, 3, 5);
