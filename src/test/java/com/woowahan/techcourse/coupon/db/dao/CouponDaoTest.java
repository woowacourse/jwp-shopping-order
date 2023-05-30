package com.woowahan.techcourse.coupon.db.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CouponDaoTest {

    private JdbcTemplate jdbcTemplate;
    private CouponDao couponDao;

    @Autowired
    void setUp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        couponDao = new CouponDao(jdbcTemplate);
    }

    @BeforeEach
    void setDummyCoupon() {
        jdbcTemplate.update("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.update(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.update("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.update(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
    }

    @ParameterizedTest
    @CsvSource(value = {"1, true", "2, false"})
    void 쿠폰_존재_여부를_반환한다(long couponId, boolean expected) {
        // given
        // when
        boolean result = couponDao.exists(couponId);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
