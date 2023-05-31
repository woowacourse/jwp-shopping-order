package cart.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.domain.Coupon;
import cart.domain.DiscountPolicy;

@Repository
public class CouponDao {

    private static final RowMapper<Coupon> COUPON_ROW_MAPPER = (rs, rowNum) ->
            new Coupon(
                    rs.getLong("id"),
                    rs.getInt("amount"),
                    DiscountPolicy.valueOf(rs.getString("discount_policy"))
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Coupon coupon) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", coupon.getId())
                .addValue("amount", coupon.getAmount())
                .addValue("discount_policy", coupon.getDiscountPolicy().name());

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public Coupon selectBy(Long id) {
        String sql = "select id, amount, discount_policy from coupon where id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                COUPON_ROW_MAPPER,
                id
        );
    }

    public List<Coupon> selectAll() {
        String sql = "select id, amount, discount_policy from coupon";

        return jdbcTemplate.query(
                sql,
                COUPON_ROW_MAPPER
        );
    }
}
