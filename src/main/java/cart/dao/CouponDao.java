package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static RowMapper<Coupon> rowMapper() {
        return (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                new Discount(rs.getString("discount_type"), rs.getInt("amount"))
        );
    }

    public CouponDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public List<Coupon> findAll() {
        String sql = "select * from coupon";
        return jdbcTemplate.query(sql, rowMapper());
    }

    public Coupon findById(Long id) {
        String sql = "select * from coupon where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public List<Coupon> findById(List<Long> ids) {
        final String sql = "SELECT * FROM coupon WHERE id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedJdbcTemplate.query(sql, parameters, rowMapper());
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

    public void delete(List<Long> ids) {
        final String sql = "delete from coupon where id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        namedJdbcTemplate.update(sql, parameters);
    }
}
