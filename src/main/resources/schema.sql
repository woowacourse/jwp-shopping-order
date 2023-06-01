CREATE TABLE if not exists product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    stock     INT          NOT NULL
);

CREATE TABLE if not exists member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    /*적립률은 클래스(코드) 상에서 관리*/
);

CREATE TABLE if not exists point
(
    id           BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    earned_point INT      NOT NULL,
    left_point   INT      NOT NULL,
    member_id    BIGINT   NOT NULL,
    expired_at   DATETIME NOT NULL,
    created_at   DATETIME NOT NULL
);

CREATE TABLE if not exists cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL
);

CREATE TABLE if not exists orders
(
    id           BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT   NOT NULL,
    point_id     BIGINT   NOT NULL,
    earned_point INT      NOT NULL,
    used_point   INT      NOT NULL,
    created_at   DATETIME NOT NULL
);

CREATE TABLE if not exists order_detail
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    orders_id         BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    order_quantity    INT          NOT NULL
);
