CREATE TABLE if not exists product
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    point_ratio DOUBLE NOT NULL,
    point_available BOOLEAN
);

CREATE TABLE if not exists member
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    point BIGINT NOT NULL
);

CREATE TABLE if not exists cart_item
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL
);

CREATE TABLE if not exists orders
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    original_price BIGINT NOT NULL,
    used_point BIGINT NOT NULL,
    point_to_add BIGINT NOT NULL
);

CREATE TABLE if not exists order_info
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL
);
