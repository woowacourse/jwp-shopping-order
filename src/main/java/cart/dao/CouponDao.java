package cart.dao;

import cart.dao.entity.CouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) -> new CouponEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("min_order_price"),
            rs.getInt("max_discount_price"),
            rs.getString("type"),
            rs.getInt("discount_amount"),
            rs.getDouble("discount_percentage")
    );

    public List<CouponEntity> findAll() {
        final String sql = "SELECT * FROM coupon";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<CouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch(EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
