package cart.dao;

import cart.domain.Coupon;
import cart.domain.CouponType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Coupon> rowMapper = (resultSet, rowNum) -> new Coupon(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            CouponType.from(resultSet.getString("type")),
            resultSet.getInt("figure")
    );

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon findById(long couponId) {
        String sql = "SELECT id, name, type, figure FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{couponId}, rowMapper);
    }
}
