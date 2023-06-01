package cart.dao.policy;

import cart.entity.policy.PolicyEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    // 추후 사용할 예정입니다 :)
    private final RowMapper<PolicyEntity> rowMapper = (rs, rowNum) ->
            new PolicyEntity(
                    rs.getLong("id"),
                    rs.getBoolean("isPercentage"),
                    rs.getInt("amount")
            );

    public long createProductDefaultPolicy() {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO policy (isPercentage, amount) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setBoolean(1, false);
            ps.setInt(2, 0);

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

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
}
