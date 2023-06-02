USE `shopping-order`;
# 멤버

CREATE TABLE IF NOT EXISTS member
(
    id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name     VARCHAR(10)  NOT NULL UNIQUE, # 4~10
    password VARCHAR(64)  NOT NULL,        # 4~10 (넣을 때는 인코딩된 값을 넣는 걸루), SHA256으로 암호화
    PRIMARY KEY (id)
);

# 상품
CREATE TABLE IF NOT EXISTS product
(
    id        INT UNSIGNED AUTO_INCREMENT,
    name      VARCHAR(20)  NOT NULL, # 1~20
    price     INT          NOT NULL, # 1 ~ 10,000,000 (할인 시 금액 내림)
    image_url VARCHAR(512) NOT NULL,
    PRIMARY KEY (id)
);

# 장바구니 아이템
CREATE TABLE IF NOT EXISTS cart_item
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id  INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,
    quantity   INT          NOT NULL, # 1 ~ 1000개
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

# 쿠폰
CREATE TABLE IF NOT EXISTS coupon
(
    id            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name          VARCHAR(50)  NOT NULL, # 1 ~ 50자
    discount_rate INT          NOT NULL, # 5 ~ 90%
    `period`      INT          NOT NULL, # 1 ~ 365일
    expired_at    DATETIME     NOT NULL, # 최대 만료 기간 (LocalDateTime.MAX)
    UNIQUE (name, discount_rate),
    PRIMARY KEY (id)
);

# 멤버 쿠폰
CREATE TABLE IF NOT EXISTS member_coupon
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id  INT UNSIGNED NOT NULL,
    coupon_id  INT UNSIGNED NOT NULL,
    issued_at  DATETIME     NOT NULL, # LocalDateTime.NOW()
    expired_at DATETIME     NOT NULL, # issued_date + period (coupon), 쿠폰의 expired_date보다 작거나 같은 값
    is_used    TINYINT(1)   NOT NULL,
    UNIQUE (member_id, coupon_id),
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);

# 주문 - 주문할 때 총 상품 개수는 최대 1000개
CREATE TABLE IF NOT EXISTS `order`
(
    id                     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    member_id              INT UNSIGNED NOT NULL,
    total_price            INT UNSIGNED NOT NULL,
    discounted_total_price INT UNSIGNED NOT NULL,
    delivery_price         INT UNSIGNED NOT NULL,
    order_at               DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
# 주문 상품
CREATE TABLE IF NOT EXISTS order_product
(
    id                    INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    order_id              INT UNSIGNED    NOT NULL,
    product_id            INT UNSIGNED    NOT NULL,
    ordered_product_price INT UNSIGNED NOT NULL,
    quantity              INT             NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

# 주문 쿠폰
CREATE TABLE IF NOT EXISTS order_coupon
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id  INT UNSIGNED NOT NULL,
    coupon_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES `order` (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (id)
);
