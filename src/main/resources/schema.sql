CREATE TABLE IF NOT EXISTS `PRODUCT`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name`       varchar(255)     NOT NULL,
    `image_url`  varchar(1024)    NOT NULL,
    `price`      long             NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `MEMBER`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `email`      varchar(255)     NOT NULL UNIQUE,
    `password`   varchar(255)     NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `CART_ITEM`
(
    `id`         long PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `member_id`  long             NOT NULL,
    `product_id` long             NOT NULL,
    `quantity`   long             NOT NULL,
    `created_at` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT CART_UNIQUE UNIQUE (`member_id`, `product_id`),
    FOREIGN KEY (`member_id`) REFERENCES `MEMBER` (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `PRODUCT` (`id`)
);
