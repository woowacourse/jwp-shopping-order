package cart.discountpolicy.application;

import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.application.builder.DiscountPolicyBuilder;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DiscountPolicyDao implements DiscountPolicyRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public DiscountPolicyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("DISCOUNT_POLICY")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long save(DiscountCondition discountCondition) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("target", discountCondition.getDiscountTarget().name());
        parameterSource.addValue("unit", discountCondition.getDiscountUnit().name());
        parameterSource.addValue("discount_value", discountCondition.getDiscountValue());
        final var discountPolicyId = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();

        if (discountCondition.getDiscountTarget() == DiscountTarget.SPECIFIC) {
            for (Long productId : discountCondition.getDiscountTargetProductIds()) {
                final var sql = "insert into POLICY_PRODUCTS (product_id, discount_policy_id) values (?, ?)";
                jdbcTemplate.update(sql, productId, discountPolicyId);
            }
        }

        return discountPolicyId;
    }

    @Override
    public DiscountPolicy findById(Long id) {
        final var sql = "select * from DISCOUNT_POLICY where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> findDiscountPolicy(rs), id);
    }

    private DiscountPolicy findDiscountPolicy(ResultSet rs) throws SQLException {
        final var target = DiscountTarget.valueOf(rs.getString("target"));

        if (target == DiscountTarget.SPECIFIC) {
            return getDiscountPolicyForSpecificProducts(rs);
        }

        final var discountCondition = DiscountCondition.from(
                DiscountTarget.valueOf(rs.getString("target")),
                DiscountUnit.valueOf(rs.getString("unit")),
                rs.getInt("discount_value")
        );
        return DiscountPolicyBuilder.build(discountCondition);
    }

    private DiscountPolicy getDiscountPolicyForSpecificProducts(ResultSet rs) throws SQLException {
        final var sql = "select product_id from POLICY_PRODUCTS where discount_policy_id = ?";
        final var productIds = jdbcTemplate.query(sql, (rs1, rowNum) -> rs1.getLong("product_id"), rs.getLong("id"));

        final var discountCondition = DiscountCondition.makeConditionForSpecificProducts(
                productIds,
                DiscountUnit.valueOf(rs.getString("unit")),
                rs.getInt("discount_value")
        );

        return DiscountPolicyBuilder.build(discountCondition);
    }
}
