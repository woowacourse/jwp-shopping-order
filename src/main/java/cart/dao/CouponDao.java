package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void updateUsageStatus(final Long memberId, final Long couponId) {
        String sql = "UPDATE coupon " +
                "SET usage_status = 'N' " +
                "WHERE member_id = ? " +
                "AND id = ?";
        jdbcTemplate.update(sql, memberId, couponId);
    }

    public Long create(final Long memberId, final Long couponTypeId) {
        String sql = "INSERT INTO coupon(usage_status, member_id, coupon_type_id) VALUES (?, ?, ?) ";
        return (long) jdbcTemplate.update(sql, "N", memberId, couponTypeId);
    }
}
