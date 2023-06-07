package cart.dao;

import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Component
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    private static RowMapper<MemberCouponEntity> getCouponRowMapper() {
        return (rs, rowNum) -> new MemberCouponEntity(rs.getLong("member_coupon.id"),
                new CouponEntity(
                        rs.getLong("coupon.id"),
                        rs.getString("name"),
                        rs.getString("discount_type"),
                        rs.getInt("amount")
                ),
                rs.getBoolean("used"));
    }

    public List<MemberCouponEntity> findByMemberId(Long memberId) {
        try {
            String sql = "SELECT * FROM coupon INNER JOIN member_coupon ON coupon.id = member_coupon.coupon_id WHERE member_coupon.member_id = ?";
            return jdbcTemplate.query(sql, getCouponRowMapper(), memberId);
        } catch (final EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public List<MemberCouponEntity> findByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            String sql = "SELECT * FROM coupon INNER JOIN member_coupon ON coupon.id = member_coupon.coupon_id WHERE member_coupon.id IN (:ids)";

            SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
            return namedJdbcTemplate.query(sql, parameters, getCouponRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Long create(Long memberId, Long couponId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("coupon_id", couponId);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void updateCoupon(List<MemberCouponEntity> memberCoupons, Long memberId) {
        if (memberCoupons.isEmpty()) {
            return;
        }

        String sql = "UPDATE member_coupon SET member_id = ?, coupon_id = ?, used = ? where id = ?";

        jdbcTemplate.batchUpdate(sql,
                memberCoupons,
                memberCoupons.size(),
                (PreparedStatement ps, MemberCouponEntity memberCoupon) -> {
                    ps.setLong(1, memberId);
                    ps.setLong(2, memberCoupon.getCoupon().getId());
                    ps.setBoolean(3, memberCoupon.isUsed());
                    ps.setLong(4, memberCoupon.getId());

                });
    }
}
