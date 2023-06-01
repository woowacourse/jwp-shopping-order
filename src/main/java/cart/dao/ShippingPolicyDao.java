package cart.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShippingPolicyDao {

    private final JdbcTemplate jdbcTemplate;

    public ShippingPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> selectDiscountThreshold(){
        try{
            String sql = "SELECT threshold FROM shipping_discount_policy WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, 1));
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }
}
