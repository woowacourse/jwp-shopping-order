package cart.dao;

import cart.domain.DefaultDeliveryPolicy;
import cart.domain.DeliveryPolicy;
import cart.domain.Money;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppliedDefaultDeliveryPolicyDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<DeliveryPolicy> deliveryPolicyRowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        Money fee = Money.from(rs.getInt("fee"));
        return new DefaultDeliveryPolicy(id, name, fee);
    };

    public AppliedDefaultDeliveryPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("applied_default_delivery_policy")
                .usingGeneratedKeyColumns("id");
    }

    public void insert(final long paymentRecordId, final Long policyId) {
        final Map<String, Object> parameters = Map.of("payment_record_id", paymentRecordId, "default_delivery_policy_id", policyId);
        this.simpleJdbcInsert.execute(parameters);
    }

    public DeliveryPolicy findByPaymentRecordId(final Long paymentRecordId) {
        final String sql = "SELECT B.id AS id, B.name AS name, B.fee AS fee FROM applied_default_delivery_policy AS A " +
                "INNER JOIN default_delivery_policy AS B ON A.default_delivery_policy_id = B.id " +
                "WHERE A.payment_record_id = ?";
        return this.jdbcTemplate.queryForObject(sql, this.deliveryPolicyRowMapper, paymentRecordId);
    }
}
