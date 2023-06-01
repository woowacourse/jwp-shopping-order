package com.woowahan.techcourse.order.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowahan.techcourse.order.domain.OrderFixture;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
@Transactional
class CouponPriceCalculatorTest {

    @Autowired
    private CouponPriceCalculator couponPriceCalculator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(
                "INSERT INTO product (id, name, price, image_url) VALUES (1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.execute(
                "INSERT INTO product (id, name, price, image_url) VALUES (2, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.execute(
                "INSERT INTO product (id, name, price, image_url) VALUES (3, '피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.execute("INSERT INTO member (id, email, password) VALUES (1, 'a@a.com', '1234')");
        jdbcTemplate.execute("INSERT INTO member (id, email, password) VALUES (2, 'b@b.com', '1234')");

        jdbcTemplate.execute("INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 5)");

        jdbcTemplate.execute("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.execute(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.execute("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.execute(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
        jdbcTemplate.execute("INSERT INTO coupon_member(id, coupon_id, member_id) VALUES (1, 1, 1)");
    }

    @Test
    void calculate() {
        BigDecimal result = couponPriceCalculator.calculate(OrderFixture.firstOrder);

        assertThat(result.doubleValue()).isEqualTo(new BigDecimal(900).doubleValue());
    }
}
