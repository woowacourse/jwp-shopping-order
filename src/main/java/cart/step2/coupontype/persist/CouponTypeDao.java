package cart.step2.coupontype.persist;

import cart.step2.coupontype.domain.CouponTypeEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponTypeDao {

    private RowMapper<CouponTypeEntity> rowMapper = (rs, rowNum) -> {
        return new CouponTypeEntity(
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

    public List<CouponTypeEntity> findAll() {
        String sql = "SELECT * FROM coupon_type ORDER BY discount_amount";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<CouponTypeEntity> findById(final Long couponTypeId) {
        String sql = "SELECT * FROM coupon_type WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, couponTypeId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
