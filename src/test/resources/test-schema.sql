CREATE TABLE IF NOT EXISTS product
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`    VARCHAR(255) NOT NULL,
    price     INT          NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`   VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL
);

CREATE TABLE IF NOT EXISTS `order`
(
    id            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT NOT NULL,
    total_price   INT    NOT NULL,
    payment_price INT    NOT NULL,
    point         INT      DEFAULT 0,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ordered_item
(
    id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id         BIGINT       NOT NULL,
    product_name     VARCHAR(255) NOT NULL,
    product_price    INT          NOT NULL,
    product_quantity INT          NOT NULL,
    product_image    TEXT         NOT NULL
);

CREATE TABLE IF NOT EXISTS coupon
(
    id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`           VARCHAR(255) NOT NULL,
    min_amount       INT                             DEFAULT 0,
    discount_percent INT DEFAULT 0,
    discount_amount  INT          NOT NULL              DEFAULT 0
    CONSTRAINT chk_coupon CHECK (discount_percent = 0 OR discount_amount = 0),
    CONSTRAINT chk_coupon_positive CHECK (discount_percent >= 0 AND discount_amount >= 0),
    CONSTRAINT chk_coupon_valid CHECK (discount_percent <= 100)
);

CREATE TABLE IF NOT EXISTS member_coupon
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    status    TINYINT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS point_history
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    order_id     BIGINT NOT NULL,
    used_point   INT DEFAULT 0,
    earned_point INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS ordered_coupon
(
    id               BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id         BIGINT NOT NULL,
    member_coupon_id BIGINT NOT NULL
);
