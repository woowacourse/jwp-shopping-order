create table if not exists product
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

create table if not exists member
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

create table if not exists point
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    point BIGINT NOT NULL DEFAULT 0
);

create table if not exists cart_item
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL
);

create table if not exists orders
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    shipping_fee BIGINT NOT NULL,
    total_products_price BIGINT NOT NULL,
    used_point BIGINT NOT NULL,
    created_at timestamp NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

create table if not exists order_item
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    orders_id BIGINT NOT NULL
);

create table if not exists shipping_fee
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fee BIGINT NOT NULL
);

create table if not exists shipping_discount_policy
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    threshold BIGINT NOT NULL
);
