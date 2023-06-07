CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    points   INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT  NOT NULL,
    product_id BIGINT  NOT NULL,
    quantity   INT     NOT NULL,
    checked    BOOLEAN NOT NULL
);


CREATE TABLE IF NOT EXISTS orders
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    used_points INT    NOT NULL,
    saving_rate INT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_item
(
    id                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_quantity  INT          NOT NULL,
    product_image_url TEXT         NOT NULL
);
