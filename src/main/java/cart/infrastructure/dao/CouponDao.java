package cart.infrastructure.dao;

import cart.entity.CouponEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) ->
            CouponEntity.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("min_amount"),
                    rs.getInt("discount_amount")
            );

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(final CouponEntity coupon) {
        final String couponSql = "UPDATE coupon SET name = ?, min_amount = ?, discount_amount = ? WHERE id = ?";
        jdbcTemplate.update(couponSql, coupon.getName(), coupon.getMinAmount(), coupon.getDiscountAmount(),
                coupon.getId());
    }
}
