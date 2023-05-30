package cart.dao;

import cart.domain.Coupon;
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

    private final RowMapper<Coupon> rowMapper = (rs, rowNum) -> {
        Coupon coupon = new Coupon(rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("discount_percent"));
        return coupon;
    };

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon getCouponById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Long createCoupon(Coupon coupon) {
        String sql = "INSERT INTO coupon (name, discount_percent) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, coupon.getName());
            ps.setInt(2, coupon.getDiscountPercent().getValue());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteCoupon(Long id) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.discount_percent, coupon_box.member_id " +
                "FROM coupon_box JOIN coupon ON coupon_box.coupon_id = coupon.id WHERE coupon_box.member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long couponId = rs.getLong("coupon.id");
            String couponName = rs.getString("coupon.name");
            int discountPercent = rs.getInt("coupon.discount_percent");
            return new Coupon(couponId, couponName, discountPercent);
        }, memberId);
    }


}
