package cart.dao;

import cart.domain.DefaultDiscountPolicy;
import cart.domain.DiscountPolicy;
import cart.domain.Money;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppliedDefaultDiscountPolicyDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<DiscountPolicy> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        Money threshold = Money.from(rs.getInt("threshold"));
        double discountRate = rs.getDouble("discount_rate");
        return new DefaultDiscountPolicy(id, name, threshold, discountRate);
    };

    public AppliedDefaultDiscountPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("applied_default_discount_policy")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(final long productId, final long discountPolicyId) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("product_id", productId);
        parameters.put("default_discount_policy_id", discountPolicyId);
        return this.simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public DiscountPolicy findByPaymentRecordId(final Long paymentRecordId) {
        final String sql = "SELECT B.id AS id, B.name AS name, B.threshold AS threshold, B.discount_rate AS discount_rate FROM applied_default_discount_policy AS A " +
                "INNER JOIN default_discount_policy AS B ON A.default_discount_policy_id = B.id " +
                "WHERE A.payment_record_id = ?";
        return this.jdbcTemplate.queryForObject(sql, this.rowMapper, paymentRecordId);
    }
}
