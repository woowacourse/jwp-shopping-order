package cart.dao;

import cart.dao.entity.ShippingDiscountPolicyEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShippingDiscountPolicyDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ShippingDiscountPolicyEntity> rowMapper = (rs, rowNum) ->
            new ShippingDiscountPolicyEntity(
                    rs.getLong("id"),
                    rs.getLong("threshold")
            );

    public ShippingDiscountPolicyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ShippingDiscountPolicyEntity> findShippingDiscountPolicy() {
        String sql = "select * from shipping_discount_policy";
        try {
            final ShippingDiscountPolicyEntity shippingDiscountPolicyEntity = jdbcTemplate.queryForObject(sql, rowMapper);
            return Optional.ofNullable(shippingDiscountPolicyEntity);
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
