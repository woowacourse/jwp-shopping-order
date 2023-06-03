package com.woowahan.techcourse.coupon.db.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CouponMemberDaoTest {

    private JdbcTemplate jdbcTemplate;
    private CouponMemberDao couponMemberDao;

    @Autowired
    void setUp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        couponMemberDao = new CouponMemberDao(jdbcTemplate, new CouponDao(jdbcTemplate));
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
}
