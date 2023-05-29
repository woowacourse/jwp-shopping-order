package cart.dao;

import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String policyType = rs.getString("policy_type");
        final long discountPrice = rs.getLong("discount_price");
        final int discountPercent = rs.getInt("discount_percent");
        final boolean discountDeliveryFee = rs.getBoolean("discount_delivery_fee");
        final String conditionType = rs.getString("condition_type");
        final long minimumPrice = rs.getLong("minimum_price");
        return new CouponEntity(id, name, policyType, discountPrice, discountPercent, discountDeliveryFee, conditionType, minimumPrice);
    };

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CouponEntity> findById(final Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<CouponEntity> findAll() {
        String sql = "SELECT * FROM coupon";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
