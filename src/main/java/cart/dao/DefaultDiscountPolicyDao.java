package cart.dao;

import cart.domain.DefaultDiscountPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Money;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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
            return new DefaultDiscountPolicy(id, name, Money.from(threshold), discountRate);
        });
    }
}
