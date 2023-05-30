package cart.dao;

import cart.domain.Coupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final JdbcTemplate jdbcTemplate;

    public CouponRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon findById(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE couponId = ?";
        return jdbcTemplate.query(sql,
                ps -> ps.setLong(1, couponId),
                rs -> {
                    return new Coupon(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getInt("discount_rate"),
                            rs.getInt("period"),
                            rs.getTimestamp("expired_at").toLocalDateTime()
                    );
                }
        );
    }
}
