package cart.dao.policy;

import cart.entity.policy.PolicyEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class PolicyDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PolicyDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<PolicyEntity> rowMapper = (rs, rowNum) ->
            new PolicyEntity(
                    rs.getLong("id"),
                    rs.getBoolean("isPercentage"),
                    rs.getInt("amount")
            );

    public Long createProductDefaultPolicy() {
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

    public void applySalePolicy(final long policyId, final int amount) {
        String sql = "UPDATE policy SET amount = ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, policyId);
    }

    public void deletePolicy(final long policyId) {
        String sql = "DELETE FROM policy WHERE id = ?";
        jdbcTemplate.update(sql, policyId);
    }
}
