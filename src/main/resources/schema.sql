CREATE TABLE IF NOT EXISTS product(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    price      BIGINT       NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS member(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS cart_item(
    id         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT      NOT NULL,
    product_id BIGINT      NOT NULL,
    quantity   INT         NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS coupon(
    id            BIGINT                              NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255)                        NOT NULL,
    discount_type ENUM ('PERCENTAGE', 'FIXED_AMOUNT') NOT NULL,
    discount      BIGINT                              NOT NULL,
    created_at    DATETIME(6)                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME(6)                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS coupon_serial_number(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    coupon_id     BIGINT       NOT NULL,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    is_issued     BOOLEAN      NOT NULL,
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS member_coupon(
    id                      BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id               BIGINT      NOT NULL,
    coupon_serial_number_id BIGINT      NOT NULL,
    created_at              DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS orders(
    id           BIGINT                                NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id    BIGINT                                NOT NULL,
    coupon_id    BIGINT,
    delivery_fee BIGINT                                NOT NULL,
    order_status ENUM ('PAID','CANCELED','IN_TRANSIT') NOT NULL DEFAULT 'PAID',
    created_at   DATETIME(6)                           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME(6)                           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS order_item(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id   BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    price      BIGINT       NOT NULL,
    image_url  VARCHAR(255) NOT NULL,
    quantity   INT          NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
