package cart.dao;

import cart.domain.DefaultDeliveryPolicy;
import cart.domain.DeliveryPolicy;
import cart.domain.Money;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultDeliveryPolicyDao {
    private final JdbcTemplate jdbcTemplate;

    public DefaultDeliveryPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DeliveryPolicy> findDefault() {
        final String sql = "SELECT * FROM default_delivery_policy";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            final long id = rs.getLong("id");
            final String name = rs.getString("name");
            final int fee = rs.getInt("fee");
            return new DefaultDeliveryPolicy(id, name, Money.from(fee));
        });
    }
}
