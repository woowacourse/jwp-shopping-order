package cart.dao;

import cart.dao.entity.AppliedDeliveryPolicyEntity;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class AppliedDeliveryPolicyDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<AppliedDeliveryPolicyEntity> deliveryPolicyRowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        Long orderId = rs.getLong("order_id");
        Long deliveryPolicyId = rs.getLong("delivery_policy_id");
        return new AppliedDeliveryPolicyEntity(id, orderId, deliveryPolicyId);
    };

    public AppliedDeliveryPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("applied_delivery_policy")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final long paymentRecordId, final Long policyId) {
        final Map<String, Object> parameters = Map.of("payment_record_id", paymentRecordId,
                "delivery_policy_id", policyId);
        return this.simpleJdbcInsert.executeAndReturnKeyHolder(parameters).getKeyAs(Long.class);
    }

    public AppliedDeliveryPolicyEntity findByPaymentRecordId(final Long paymentRecordId) {
        final String sql =
                "SELECT A.id AS id, P.order_id AS order_id, A.delivery_policy_id AS delivery_policy_id " +
                        "FROM applied_delivery_policy AS A " +
                        "INNER JOIN payment_record as P ON (A.payment_record_id = P.id) " +
                        "WHERE A.payment_record_id = ?";
        return this.jdbcTemplate.queryForObject(sql, this.deliveryPolicyRowMapper, paymentRecordId);
    }
}
