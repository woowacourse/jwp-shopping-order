package cart.dao.coupon;

import cart.entity.coupon.CouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CouponDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) ->
            new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("policy_id")
            );

    public CouponEntity findById(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, couponId);
    }

    public void deleteById(final long couponId) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, couponId);
    }

    public void deleteAllByIds(final List<Long> couponIds) {
        String sql = "DELETE FROM coupon WHERE id IN (:couponIds)";
        Map<String, Object> params = Collections.singletonMap("couponIds", couponIds);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public long save(final String name, final long policyId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", name);
        parameters.put("policy_id", policyId);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }

    public List<CouponEntity> findAll() {
        String sql = "SELECT * from coupon";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean isExistCouponById(final Long couponId) {
        String sql = "SELECT COUNT(*) FROM coupon WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, couponId);
        return count > 0;
    }
}
