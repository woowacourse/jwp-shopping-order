package cart.dao;

import cart.dao.entity.CouponEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CouponDao {

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CouponDao(final NamedParameterJdbcOperations jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long issue(final CouponEntity couponEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(couponEntity);
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }
}
