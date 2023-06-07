package cart.persistance.dao;

import cart.persistance.entity.CouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponDao {

    private static final RowMapper<CouponEntity> COUPON_ROW_MAPPER = (rs, rowNum) ->
            new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("coupon_name"),
                    rs.getString("discount_type"),
                    rs.getDouble("discount_value")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert couponInsert;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.couponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(CouponEntity coupon) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("coupon_name", coupon.getName())
                .addValue("discount_type", coupon.getDiscountType())
                .addValue("discount_value", coupon.getDiscountValue());

        return couponInsert.executeAndReturnKey(source).longValue();
    }

    public List<CouponEntity> findAll() {
        String sql = "select id, coupon_name, discount_type, discount_value from coupon";
        return jdbcTemplate.query(sql, COUPON_ROW_MAPPER);
    }

    public CouponEntity findById(Long id) {
        String sql = "select id, coupon_name, discount_type, discount_value from coupon where id = ?";
        return jdbcTemplate.queryForObject(sql, COUPON_ROW_MAPPER, id);
    }

    public void delete(CouponEntity coupon) {
        String sql = "delete from coupon where id = ?";
        jdbcTemplate.update(sql, coupon.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from coupon where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
