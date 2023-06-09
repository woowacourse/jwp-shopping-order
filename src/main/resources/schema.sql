CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    discount_price INT NOT NULL
);

CREATE TABLE member (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL
);

CREATE TABLE cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    discount_policy_name VARCHAR(255) NOT NULL,
    discount_value INT NOT NULL,
    category VARCHAR(255) NOT NULL

);

CREATE TABLE coupon_box (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

CREATE TABLE ordered (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    time TIMESTAMP NOT NULL,
    delivery_price INT NOT NULL,
    discount_price_from_total INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE ordered_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ordered_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    total_price INT NOT NULL,
    total_discount_price INT NOT NULL,
    FOREIGN KEY (ordered_id) REFERENCES ordered(id)
);




