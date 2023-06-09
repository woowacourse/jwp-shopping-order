package cart.dao;

import cart.domain.DefaultDiscountPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Money;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DefaultDiscountPolicyDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<DiscountPolicy> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        Money threshold = Money.from(rs.getInt("threshold"));
        BigDecimal discountRate = rs.getBigDecimal("discount_rate");
        return new DefaultDiscountPolicy(id, name, threshold, discountRate);
    };

    public DefaultDiscountPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DiscountPolicy> findDefault() {
        final String sql = "SELECT * FROM default_discount_policy";
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }
}
