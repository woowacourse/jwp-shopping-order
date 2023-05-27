package cart.dao;

import cart.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String policyType = rs.getString("policy_type");
        final long disCountPrice = rs.getLong("discount_price");
        final int discountPercent = rs.getInt("discount_percent");
        final boolean discountDeliveryFee = rs.getBoolean("discount_delivery_fee");
        final String conditionType = rs.getString("condition_type");
        final long minimumPrice = rs.getLong("minimum_price");
        return new CouponEntity(id, name, policyType, disCountPrice, discountPercent,
                discountDeliveryFee, conditionType, minimumPrice);
    };

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingColumns("name", "policy_type", "discount_price", "discount_percent",
                        "discount_delivery_fee", "condition_type", "minimum_price")
                .usingGeneratedKeyColumns("id");
    }

    public CouponEntity insert(final CouponEntity couponEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(couponEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new CouponEntity(
                id,
                couponEntity.getName(),
                couponEntity.getPolicyType(),
                couponEntity.getDiscountPrice(),
                couponEntity.getDiscountPercent(),
                couponEntity.isDiscountDeliveryFee(),
                couponEntity.getConditionType(),
                couponEntity.getMinimumPrice()
        );
    }

    public List<CouponEntity> findAll() {
        final String sql = "SELECT * FROM coupon";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<CouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
