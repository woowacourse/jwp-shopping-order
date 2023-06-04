package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbCouponBoxDao implements CouponBoxDao {

    private final JdbcTemplate jdbcTemplate;

    public DbCouponBoxDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void delete(Long memberId, Long couponId) {
        String sql = "delete from coupon_box where member_id = ? and coupon_id = ?";
        jdbcTemplate.update(sql, memberId, couponId);
    }
}
