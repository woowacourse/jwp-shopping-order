--DROP TABLE IF EXISTS ordered_item;
--DROP TABLE IF EXISTS orders;
--DROP TABLE IF EXISTS cart_item;
--DROP TABLE IF EXISTS member;
--DROP TABLE IF EXISTS product;

CREATE TABLE if not exists product
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    price         INT          NOT NULL,
    image_url     VARCHAR(255) NOT NULL,
    is_discounted tinyint      not null,
    discount_rate int          not null
);

CREATE TABLE if not exists member
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email                 VARCHAR(255) NOT NULL UNIQUE,
    password              VARCHAR(255) NOT NULL,
    grade                 varchar(255) not null,
    total_purchase_amount int          not null
);

CREATE TABLE if not exists cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    quantity   INT    NOT NULL,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

create table if not exists orders
(
    id                          bigint   not null auto_increment primary key,
    total_item_price            int      not null,
    discounted_total_item_price int      not null,
    shipping_fee                int      not null,
    ordered_at                  datetime not null,
    member_id                   bigint   not null,
    foreign key (member_id) references member (id)
);


--CREATE TABLE if not exists orders (
--    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    member_id BIGINT NOT NULL, ㅌ
--    ordered_at DATETIME NOT NULL default current_timestamp, ㅌ
--    total_item_discount_amount INT NOT NULL,
--    total_member_discount_amount INT NOT NULL,
--    total_item_price INT NOT NULL, ㅌ
--    discounted_total_item_price INT NOT NULL, ㅌ
--    shipping_fee INT NOT NULL, ㅌ
--    total_price INT NOT NULL,
--    FOREIGN KEY (member_id) REFERENCES member(id)
--);

--CREATE TABLE if not exists ordered_item (
--    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
--    orders_id BIGINT NOT NULL,
--    product_name VARCHAR(255) NOT NULL,
--    product_price INT NOT NULL,
--    product_image_url VARCHAR(255) NOT NULL,
--    product_quantity INT NOT NULL,
--    product_is_discounted TINYINT NOT NULL,
--    product_discount_rate INT,
--    FOREIGN KEY (orders_id) REFERENCES orders(id)
--);

create table if not exists order_item
(
    id            bigint       not null auto_increment primary key,
    name          varchar(255) not null,
    price         int          not null,
    image_url     varchar(255) not null,
    quantity      int          not null,
    discount_rate int          not null,
    order_id      bigint       not null,
    foreign key (order_id) references orders (id)
);
