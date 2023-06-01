package cart.dao;

import cart.domain.coupon.Coupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CouponRepository {

    private final JdbcTemplate jdbcTemplate;

    public CouponRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon findById(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new CouponRowMapper(), couponId);
    }

    private static class CouponRowMapper implements RowMapper<Coupon> {
        @Override
        public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Coupon(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("discount_rate"),
                    rs.getInt("period"),
                    rs.getTimestamp("expired_at").toLocalDateTime()
            );
        }
    }
}
