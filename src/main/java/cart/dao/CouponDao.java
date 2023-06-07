package cart.dao;

import cart.domain.Coupon;
import cart.domain.FixedDiscountCoupon;
import cart.exception.CouponNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
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

    public Coupon findUnusedById(Long couponId) throws CouponNotFoundException {
        String sql = "SELECT * FROM coupon WHERE id = ? AND used = false";

        try {
            return jdbcTemplate.queryForObject(sql, new CouponRowMapper(), couponId);
        } catch (Exception e) {
            throw new CouponNotFoundException("존재하지 않거나 이미 사용된 쿠폰 id 입니다");
        }
    }

    public void save(Coupon coupon) {
        String sql = "INSERT INTO coupon (member_id, discount_price, coupon_name, image_url)" +
                "VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, coupon.getMemberId(), coupon.getDiscountPrice().toInt(), coupon.getName(), coupon.getImageUrl());
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

    public void updateToUsed(Long couponId) throws CouponNotFoundException, DataIntegrityViolationException {
        String sql = "UPDATE coupon SET used = true WHERE id = ?";
        int updated = jdbcTemplate.update(sql, couponId);
        if (updated < 1) {
            throw new CouponNotFoundException("사용할 쿠폰 id를 찾을 수 없습니다.");
        }
        if (updated >= 2) {
            throw new DataIntegrityViolationException("중복 쿠폰이 존재합니다.");
        }
    }

    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM coupon WHERE member_id = ? AND used = false";
        return jdbcTemplate.query(sql, new Object[]{memberId}, new CouponRowMapper());
    }
}
