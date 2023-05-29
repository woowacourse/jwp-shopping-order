package cart.dao;

import cart.domain.Coupon;
import cart.domain.Discount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static RowMapper<Coupon> getCouponRowMapper() {
        return (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                new Discount(rs.getString("discount_type"), rs.getInt("amount"))
        );
    }

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public List<Coupon> findAll() {
        String sql = "select * from coupon";
        return jdbcTemplate.query(sql, getCouponRowMapper());
    }

    public Coupon findById(Long id) {
        String sql = "select * from coupon where id = ?";
        return jdbcTemplate.queryForObject(sql, getCouponRowMapper(), id);
    }

    public Long create(Coupon coupon) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", coupon.getName())
                .addValue("discount_type", coupon.getDiscount().getDiscountType().name())
                .addValue("amount", coupon.getDiscount().getAmount());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void delete(Long id) {
        String sql = "delete from coupon where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
