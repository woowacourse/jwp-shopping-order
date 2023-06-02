CREATE TABLE if not exists product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists member (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE if not exists coupon(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    discount_type varchar(255) NOT NULL,
    minimum_price INT NOT NULL,
    discount_price INT NOT NULL,
    discount_rate DOUBLE NOT NULL
);

CREATE TABLE if not exists member_coupon(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    availability BOOLEAN NOT NULL,
    FOREIGN KEY (coupon_id) REFERENCES coupon(id),
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE if not exists orders(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    original_price INT NOT NULL,
    discount_price INT NOT NULL,
    confirm_state BOOLEAN NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE if not exists order_coupon(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_coupon_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (member_coupon_id) REFERENCES member_coupon(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE if not exists order_product(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL,
    image_url varchar(255) NOT NULL,
    price INT NOT NULL,
    quantity INT NOT NULL,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) on delete cascade
);
