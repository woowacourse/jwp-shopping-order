package cart.dao;

import cart.domain.Coupon;
import cart.domain.vo.Amount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Coupon> rowMapper = new RowMapper<Coupon>() {
        @Override
        public Coupon mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                Amount.of(rs.getInt("min_amount")),
                Amount.of(rs.getInt("discount_amount")),
                rs.getBoolean("is_used")
            );
        }
    };

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Coupon> findByCouponIdAndMemberId(final long couponId, final Long memberId) {
        final String sql =
            "SELECT c.id as id, c.name as name, c.min_amount as min_amount, c.discount_amount as discount_amount, mc.is_used as is_used "
                + "FROM member_coupon as mc "
                + "INNER JOIN coupon c on mc.coupon_id = c.id "
                + "WHERE member_id = ? AND coupon_id = ?";
        final List<Coupon> coupons = jdbcTemplate.query(sql, rowMapper, couponId, memberId);
        return coupons.stream().findAny();
    }

    public void update(final Coupon usedCoupon, final Long memberId) {
        final String memberCouponSql = "UPDATE member_coupon SET is_used = ? WHERE coupon_id = ? AND member_id = ?";
        jdbcTemplate.update(memberCouponSql, usedCoupon.isUsed(), usedCoupon.getId(), memberId);

        final String couponSql = "UPDATE coupon "
            + "SET name            = ?, "
            + "    min_amount      = ?, "
            + "    discount_amount = ? "
            + "WHERE id = ?";
        jdbcTemplate.update(couponSql, usedCoupon.getName(), usedCoupon.getMinAmount(), usedCoupon.getDiscountAmount(),
            usedCoupon.getId());
    }
}
