package cart.dao;

import cart.domain.Coupon;
import cart.domain.FixedDiscountCoupon;
import cart.exception.CouponNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon findById(Long couponId) throws CouponNotFoundException {
        String sql = "SELECT id, member_id, discount_price FROM coupon WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Long id = rs.getLong("id");
                int discountPrice = rs.getInt("discount_price");
                Long member_id = rs.getLong("member_id");
                return new FixedDiscountCoupon(id, member_id, discountPrice);
            }, couponId);
        } catch (Exception e) {
            throw new CouponNotFoundException();
        }
    }


    public void delete(Long couponId) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, couponId);
    }
}
