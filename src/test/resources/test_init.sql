SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE cart_item;
ALTER TABLE cart_item AUTO_INCREMENT = 1;

TRUNCATE TABLE member;
ALTER TABLE member AUTO_INCREMENT = 1;

TRUNCATE TABLE product;
ALTER TABLE product AUTO_INCREMENT = 1;

TRUNCATE TABLE cart_item;
ALTER TABLE cart_item AUTO_INCREMENT = 1;

TRUNCATE TABLE orders;
ALTER TABLE orders AUTO_INCREMENT = 1;

TRUNCATE TABLE orders_item;
ALTER TABLE orders_item AUTO_INCREMENT = 1;

TRUNCATE TABLE point;
ALTER TABLE point AUTO_INCREMENT = 1;

TRUNCATE TABLE point_history;
ALTER TABLE point_history AUTO_INCREMENT = 1;

TRUNCATE TABLE orders_status;
ALTER TABLE orders_status AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO orders_status(status) VALUES ('Pending');
INSERT INTO orders_status(status) VALUES ('Processing');
INSERT INTO orders_status(status) VALUES ('Shipped');
INSERT INTO orders_status(status) VALUES ('Delivered');
INSERT INTO orders_status(status) VALUES ('Canceled');
