drop table if exists order_item;
drop table if exists orders;
drop table if exists coupon;
drop table if exists cart_item;
drop table if exists product;
drop table if exists member;

CREATE TABLE if not exists product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists member (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     money BIGINT NOT NULL default 0
);

CREATE TABLE if not exists cart_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE if not exists coupon (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    coupon_name varchar(30) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    discount_price BIGINT NOT NULL,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE if not exists orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    price BIGINT NOT NULL,
    discounted BIGINT default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE if not exists order_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id BIGINT NOT NULL,
    cart_item_id BIGINT NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders(id),
    FOREIGN KEY (cart_item_id) REFERENCES cart_item(id)
);
