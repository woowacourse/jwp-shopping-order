--DROP TABLE IF EXISTS ordered_item;
--DROP TABLE IF EXISTS orders;
--DROP TABLE IF EXISTS cart_item;
--DROP TABLE IF EXISTS member;
--DROP TABLE IF EXISTS product;

CREATE TABLE if not exists product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_discounted TINYINT NOT NULL,
    discount_rate INT NOT NULL
);

CREATE TABLE if not exists member (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     grade VARCHAR(255),
     total_price BIGINT NOT NULL
);

CREATE TABLE if not exists cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE if not exists orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL, ㅌ
    ordered_at DATETIME NOT NULL default current_timestamp,ㅌ
    total_item_discount_amount INT NOT NULL,
    total_member_discount_amount INT NOT NULL,
    total_item_price INT NOT NULL, ㅌ
    discounted_total_item_price INT NOT NULL,ㅌ
    shipping_fee INT NOT NULL, ㅌ
    total_price INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE if not exists ordered_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    product_quantity INT NOT NULL,
    product_is_discounted TINYINT NOT NULL,
    product_discount_rate INT,
    FOREIGN KEY (orders_id) REFERENCES orders(id)
);
