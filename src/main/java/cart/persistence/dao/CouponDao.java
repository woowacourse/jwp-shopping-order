package cart.persistence.dao;

import cart.persistence.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public long create(final CouponEntity coupon) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(coupon);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public Optional<CouponEntity> findById(final long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";

        try {
            CouponEntity coupon = jdbcTemplate.queryForObject(sql, RowMapperHelper.couponRowMapper(), id);
            return Optional.of(coupon);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<CouponEntity> findAll() {
        String sql = "SELECT * FROM coupon";
        return jdbcTemplate.query(sql, RowMapperHelper.couponRowMapper());
    }
}
