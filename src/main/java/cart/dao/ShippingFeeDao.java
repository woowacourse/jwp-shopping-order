package cart.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShippingFeeDao {

    private final JdbcTemplate jdbcTemplate;

    public ShippingFeeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> selectShippingFee(){
        try{
            String sql = "SELECT fee FROM shipping_fee WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, 1));
        } catch (EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }
}
