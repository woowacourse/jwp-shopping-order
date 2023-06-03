package cart.dao;

import cart.entity.CouponEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static RowMapper<CouponEntity> rowMapper() {
        return (rs, rowNum) -> new CouponEntity(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("discount_type"),
                rs.getInt("amount")
        );
    }

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public List<CouponEntity> findAll() {
        try {
            String sql = "select * from coupon";
            return jdbcTemplate.query(sql, rowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Long create(CouponEntity coupon) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", coupon.getName())
                .addValue("discount_type", coupon.getDiscountType())
                .addValue("amount", coupon.getAmount());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void delete(Long id) {
        String sql = "delete from coupon where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
