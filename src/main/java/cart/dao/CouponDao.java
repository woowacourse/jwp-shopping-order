package cart.dao;

import cart.entity.CouponEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String policyType = rs.getString("policy_type");
        final Long discountPrice = rs.getLong("discount_price");
        final Long minimumPrice = rs.getLong("minimum_price");
        return new CouponEntity(id, name, policyType, discountPrice, minimumPrice);
    };

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingColumns("name", "policy_type", "discount_price", "minimum_price")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<CouponEntity> findByCouponId(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, couponId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public CouponEntity insert(final CouponEntity couponEntity) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(couponEntity);
        long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new CouponEntity(id, couponEntity.getName(), couponEntity.getPolicyType(),
                couponEntity.getDiscountPrice(),
                couponEntity.getMinimumPrice());
    }
}
