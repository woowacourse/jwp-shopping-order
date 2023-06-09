package cart.dao;

import cart.dao.entity.ShippingFeeEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShippingFeeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ShippingFeeEntity> rowMapper = (rs, rowNum) ->
            new ShippingFeeEntity(
                    rs.getLong("id"),
                    rs.getLong("fee")
            );

    public ShippingFeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ShippingFeeEntity> findFee() {
        String sql = "select * from shipping_fee";
        try {
            final ShippingFeeEntity shippingFeeEntity = jdbcTemplate.queryForObject(sql, rowMapper);
            return Optional.ofNullable(shippingFeeEntity);
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
