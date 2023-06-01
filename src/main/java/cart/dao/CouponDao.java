package cart.dao;

import cart.domain.Coupon;
import cart.domain.FixedDiscountCoupon;
import cart.exception.CouponNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon findById(Long couponId) throws CouponNotFoundException {
        String sql = "SELECT * FROM coupon WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new CouponRowMapper(), couponId);
        } catch (Exception e) {
            throw new CouponNotFoundException();
        }
    }

    private static class CouponRowMapper implements RowMapper<Coupon> {
        @Override
        public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FixedDiscountCoupon(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getString("coupon_name"),
                    rs.getString("image_url"),
                    rs.getInt("discount_price")
            );
        }
    }

    public void delete(Long couponId) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, couponId);
    }

    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, discount_price, created_at, updated_at FROM coupon WHERE member_id = ?";

        return jdbcTemplate.query(sql, new Object[]{memberId}, new CouponRowMapper());
    }
}
