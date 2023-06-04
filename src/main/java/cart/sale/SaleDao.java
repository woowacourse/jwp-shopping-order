package cart.sale;

import cart.discountpolicy.application.DiscountPolicyRepository;
import cart.discountpolicy.discountcondition.DiscountTarget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SaleDao implements SaleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final DiscountPolicyRepository discountPolicyRepository;

    public SaleDao(DataSource dataSource, DiscountPolicyRepository discountPolicyRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SALE")
                .usingGeneratedKeyColumns("id");
        this.discountPolicyRepository = discountPolicyRepository;
    }

    @Override
    public Long save(String name, Long discountPolicyId) {
        final var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("discount_policy_id", discountPolicyId);
        parameterSource.addValue("name", name);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    @Override
    public Sale findById(Long id) {
        final var sql = "select * from SALE where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Sale(
                rs.getLong("id"),
                rs.getString("name"),
                discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ));
    }

    @Override
    public List<Sale> findAllExcludingTarget(List<DiscountTarget> discountTargets) {
        final var sql = "select * from SALE S " +
                "inner join DISCOUNT_POLICY DP on DP.id = S.discount_policy_id " +
                "where DP.target != 'TOTAL'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Sale(
                rs.getLong("id"),
                rs.getString("name"),
                discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ));
    }

    @Override
    public List<Sale> findAllApplyingToTotalPrice() {
        final var sql = "select * from SALE S " +
                "inner join DISCOUNT_POLICY DP on DP.id = S.discount_policy_id " +
                "where DP.target = 'TOTAL'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Sale(
                rs.getLong("id"),
                rs.getString("name"),
                discountPolicyRepository.findById(rs.getLong("discount_policy_id"))
        ));
    }
}
