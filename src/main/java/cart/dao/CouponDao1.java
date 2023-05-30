package cart.dao;

import cart.domain.Coupon;
import cart.domain.Coupon1;
import cart.domain.policy.DiscountPolicy;
import cart.domain.policy.DiscountPolicyResolver;
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
public class CouponDao1 {
    private final JdbcTemplate jdbcTemplate;

    public CouponDao1(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Coupon1> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String discount_policy_name = rs.getString("discount_policy_name");
        int discountValue = rs.getInt("discount_value");
        DiscountPolicy discountPolicy = DiscountPolicyResolver.of(discount_policy_name, discountValue);
        return new Coupon1(id, name, discountPolicy);
    };

    public Coupon1 getCouponById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Long createCoupon(Coupon1 coupon) {
        String sql = "INSERT INTO coupon (name, discount_policy_name, discount_value) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, coupon.getName());
            ps.setString(2, coupon.getDiscountPolicyName());
            ps.setInt(3, coupon.getDiscountValue());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteCoupon(Long id) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Coupon1> findByMemberId(Long memberId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.discount_policy_name, coupon.discount_value, coupon_box.member_id " +
                "FROM coupon_box JOIN coupon ON coupon_box.coupon_id = coupon.id WHERE coupon_box.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
