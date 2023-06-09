package cart.dao;

import cart.dao.entity.AppliedDiscountPolicyEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class AppliedDiscountPolicyDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<AppliedDiscountPolicyEntity> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        Long orderId = rs.getLong("order_id");
        Long policyId = rs.getLong("discount_policy_id");
        return new AppliedDiscountPolicyEntity(id, orderId, policyId);
    };

    public AppliedDiscountPolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("applied_discount_policy")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final long paymentEntityId, final long discountPolicyId) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("payment_id", paymentEntityId);
        parameters.put("discount_policy_id", discountPolicyId);
        return this.simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public List<AppliedDiscountPolicyEntity> findByPaymentEntityId(final Long paymentEntityId) {
        final String sql =
                "SELECT A.id AS id, P.order_id AS order_id, A.discount_policy_id AS discount_policy_id " +
                        "FROM applied_discount_policy AS A " +
                        "INNER JOIN payment as P ON (A.payment_id = P.id) " +
                        "WHERE A.payment_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, paymentEntityId);
    }
}
