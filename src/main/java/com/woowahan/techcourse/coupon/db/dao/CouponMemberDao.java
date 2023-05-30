package com.woowahan.techcourse.coupon.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponMemberDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean exists(Long couponId, Long memberId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT * FROM coupon_member WHERE coupon_id = ? AND member_id = ?)", Boolean.class,
                couponId, memberId);
    }

    public void insert(Long couponId, Long memberId) {
        jdbcTemplate.update("INSERT INTO coupon_member (coupon_id, member_id) VALUES (?, ?)", couponId, memberId);
    }

    public void delete(Long couponId, Long memberId) {
        jdbcTemplate.update("DELETE FROM coupon_member WHERE coupon_id = ? AND member_id = ?", couponId, memberId);
    }
}
