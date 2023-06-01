package cart.dao.policy;

import cart.entity.policy.PolicyEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PolicyDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public PolicyDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("policy")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<PolicyEntity> rowMapper = (rs, rowNum) ->
            new PolicyEntity(
                    rs.getLong("id"),
                    rs.getBoolean("isPercentage"),
                    rs.getInt("amount")
            );

    public void updateAmount(final long policyId, final int amount) {
        String sql = "UPDATE policy SET amount = ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, policyId);
    }

    public void deletePolicy(final long policyId) {
        String sql = "DELETE FROM policy WHERE id = ?";
        jdbcTemplate.update(sql, policyId);
    }

    public PolicyEntity findById(final long policyId) {
        String sql = "SELECT * FROM policy WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{policyId}, (rs, rowNum) -> {
            long id = rs.getLong("id");
            boolean isPercentage = rs.getBoolean("isPercentage");
            int amount = rs.getInt("amount");
            return new PolicyEntity(id, isPercentage, amount);
        });
    }

    public long createProductSalePolicy(final int amount) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("isPercentage", true);
        parameters.put("amount", amount);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }

    public boolean isExistSamePolicy(final boolean isPercentage, final int amount) {
        String sql = "SELECT COUNT(*) FROM policy WHERE isPercentage = ? AND amount = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, isPercentage, amount);
        return count > 0;
    }

    public long createPolicy(final boolean isPercentage, final int amount) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("isPercentage", isPercentage);
        parameters.put("amount", amount);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return generatedId.longValue();
    }

    public PolicyEntity findByIsPercentageAndAmount(final boolean isPercentage, final int amount) {
        String sql = "SELECT * FROM policy WHERE isPercentage= ? AND amount = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, isPercentage, amount);
    }
}
