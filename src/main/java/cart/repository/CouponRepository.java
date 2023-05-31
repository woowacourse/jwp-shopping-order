package cart.repository;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponRepository {

    private final DiscountPolicyProvider discountPolicyProvider;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert couponInsert;

    public CouponRepository(DiscountPolicyProvider discountPolicyProvider, JdbcTemplate jdbcTemplate) {
        this.discountPolicyProvider = discountPolicyProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.couponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Coupon coupon) {
        DiscountType discountType = discountPolicyProvider.getDiscountType(coupon.getDiscountPolicy());
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("coupon_name", coupon.getName())
                .addValue("discount_type", discountType.name())
                .addValue("discount_value", coupon.getDiscountValue());

        return couponInsert.executeAndReturnKey(source).longValue();
    }

    public List<Coupon> findAll() {
        String sql = "select id, coupon_name, discount_type, discount_value from coupon";
        return jdbcTemplate.query(sql, getCouponRowMapper());
    }

    private RowMapper<Coupon> getCouponRowMapper() {
        return (rs, rowNum) -> {
            String typeName = rs.getString("discount_type");
            DiscountPolicy discountPolicy = discountPolicyProvider.getByType(DiscountType.valueOf(typeName));
            return new Coupon(
                    rs.getLong("id"),
                    rs.getString("coupon_name"),
                    discountPolicy,
                    rs.getDouble("discount_value")
            );
        };
    }

    public Coupon findById(Long id) {
        String sql = "select id, coupon_name, discount_type, discount_value from coupon where id = ?";
        return jdbcTemplate.queryForObject(sql, getCouponRowMapper(), id);
    }

    public void delete(Coupon coupon) {
        String sql = "delete from coupon where id = ?";
        jdbcTemplate.update(sql, coupon.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from coupon where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
