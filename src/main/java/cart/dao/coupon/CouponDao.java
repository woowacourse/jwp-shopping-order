package cart.dao.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponResolver;
import cart.domain.disount.DiscountPolicy;
import cart.domain.disount.DiscountPolicyResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Coupon> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String discount_policy_name = rs.getString("discount_policy_name");
        int discountValue = rs.getInt("discount_value");
        String category = rs.getString("category");
        DiscountPolicy discountPolicy = DiscountPolicyResolver.of(discount_policy_name, discountValue);
        return CouponResolver.of (id, name, discountPolicy, category);
    };

    public Coupon getCouponById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Long createCoupon(Coupon coupon) {
        String sql = "INSERT INTO coupon (name, discount_policy_name, discount_value, category) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, coupon.getName());
            ps.setString(2, coupon.getDiscountPolicyName());
            ps.setInt(3, coupon.getDiscountValue());
            ps.setString(4, coupon.getCategory());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteCoupon(Long id) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.discount_policy_name, coupon.discount_value, coupon.category, coupon_box.member_id " +
                "FROM coupon_box JOIN coupon ON coupon_box.coupon_id = coupon.id WHERE coupon_box.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
