package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long memberId, long couponId) {
        String sql = "INSERT INTO member_coupon (member_id, coupon_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, memberId, couponId);
    }
}
