package cart.dao;

import cart.domain.DiscountPolicy;
import cart.domain.Money;
import cart.domain.UpperThresholdPriceDiscountPolicy;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultDiscountPolicyDao {
    private final JdbcTemplate jdbcTemplate;

    public DefaultDiscountPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DiscountPolicy> findDefault() {
        final String sql = "SELECT * FROM default_discount_policy";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            final long id = rs.getLong("id");
            final String name = rs.getString("name");
            final int threshold = rs.getInt("threshold");
            final double discountRate = rs.getDouble("discount_rate");
            return new UpperThresholdPriceDiscountPolicy(id, name, Money.from(threshold), discountRate);
        });
    }
}
