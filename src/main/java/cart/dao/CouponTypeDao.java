package cart.dao;

import cart.domain.CouponType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        String sql = "SELECT * FROM coupon_type ORDER BY discount_amount";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<CouponType> findById(final Long couponTypeId) {
        String sql = "SELECT * FROM coupon_type WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, couponTypeId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
