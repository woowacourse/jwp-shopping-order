package com.woowahan.techcourse.coupon.db.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponMemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CouponMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
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

    public void deleteByMemberId(long memberId, List<Long> couponIds) {
        String sql = "DELETE FROM coupon_member WHERE member_id =(:memberId) AND coupon_id IN (:couponIds)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("memberId", memberId);
        mapSqlParameterSource.addValue("couponIds", couponIds);
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public int countByMemberIdAndCouponIds(long memberId, List<Long> couponIds) {
        String sql = "SELECT COUNT(*) FROM coupon_member WHERE member_id =(:memberId) AND coupon_id IN (:couponIds)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("memberId", memberId);
        mapSqlParameterSource.addValue("couponIds", couponIds);
        return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
    }
}
