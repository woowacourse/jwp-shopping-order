package cart.db.dao;

import cart.db.entity.CouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CouponEntity findById(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new CouponEntityRowMapper(), couponId);
    }

    private static class CouponEntityRowMapper implements RowMapper<CouponEntity> {
        @Override
        public CouponEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("discount_rate"),
                    rs.getInt("period"),
                    rs.getTimestamp("expired_at").toLocalDateTime()
            );
        }
    }
}
