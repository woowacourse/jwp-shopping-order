package com.woowahan.techcourse.coupon.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean exists(Long couponId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT * FROM coupon WHERE id = ?)", Boolean.class,
                couponId);
    }
}
