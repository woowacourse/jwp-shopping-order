CREATE TABLE IF NOT EXISTS `PRODUCT`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name`       varchar(255)     NOT NULL,
    `image_url`  varchar(1024)    NOT NULL,
    `price`      long             NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS `MEMBER`
(
    `id`         long PRIMARY KEY    NOT NULL AUTO_INCREMENT,
    `email`      varchar(255) UNIQUE NOT NULL,
    `password`   varchar(255)        NOT NULL,
    `created_at` timestamp           NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` timestamp           NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS `CART_ITEM`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `member_id`  long             NOT NULL,
    `product_id` long             NOT NULL,
    `quantity`   long             NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS `ORDERS`
(
    `id`               long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `delivery_fee`     long             NOT NULL,
    `member_coupon_id` long,
    `member_id`        long             NOT NULL,
    `created_at`       timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at`       timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS `ORDER_ITEM`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name`       varchar(255)     NOT NULL,
    `image_url`  varchar(1024)    NOT NULL,
    `price`      long             NOT NULL,
    `quantity`   long             NOT NULL,
    `order_id`   long             NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS `COUPON`
(
    `id`             long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name`           varchar(255)     NOT NULL,
    `policy_type`    varchar(255)     NOT NULL,
    `discount_price` long             NOT NULL,
    `minimum_price`  long             NOT NULL,
    `created_at`     timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at`     timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS `MEMBER_COUPON`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `coupon_id`  long             NOT NULL,
    `member_id`  long             NOT NULL,
    `used`       boolean          NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` timestamp        NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE (CURRENT_TIMESTAMP)
);

ALTER TABLE `CART_ITEM`
    ADD FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`id`);

ALTER TABLE `CART_ITEM`
    ADD FOREIGN KEY (`product_id`) REFERENCES `PRODUCT` (`id`);

ALTER TABLE `ORDERS`
    ADD FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`id`);

ALTER TABLE `ORDER_ITEM`
    ADD FOREIGN KEY (`order_id`) REFERENCES `ORDERS` (`id`);

ALTER TABLE `MEMBER_COUPON`
    ADD FOREIGN KEY (`coupon_id`) REFERENCES `COUPON` (`id`);

ALTER TABLE `MEMBER_COUPON`
    ADD FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`id`);
