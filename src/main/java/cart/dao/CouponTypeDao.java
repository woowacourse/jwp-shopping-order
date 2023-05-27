package cart.dao;

import cart.domain.CouponType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponTypeDao {

    private RowMapper<CouponType> rowMapper = (rs, rowNum) -> {
        return new CouponType(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("discount_amount")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public CouponTypeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CouponType> findAll() {
        String sql = "SELECT * FROM coupon_type";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
