package cart.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<MemberCoupon> findByMemberId(Long memberId) {
        String query = "SELECT mc.id, m.id AS member_id, m.email, m.password, m.nickname, " +
            "c.id AS coupon_id, c.name, c.min_order_price, c.max_discount_price, c.type, " +
            "c.discount_amount, c.discount_percentage, mc.expired_at " +
            "FROM member_coupon mc " +
            "INNER JOIN member m ON mc.member_id = m.id " +
            "INNER JOIN coupon c ON mc.coupon_id = c.id " +
            "WHERE mc.member_id = ?";

        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            Long memberCouponId = resultSet.getLong("id");
            Long memberCouponMemberId = resultSet.getLong("member_id");
            String memberEmail = resultSet.getString("email");
            String memberPassword = resultSet.getString("password");
            String memberNickname = resultSet.getString("nickname");
            Long memberCouponCouponId = resultSet.getLong("coupon_id");
            String couponName = resultSet.getString("name");
            Integer minOrderPrice = resultSet.getInt("min_order_price");
            Integer maxDiscountPrice = resultSet.getInt("max_discount_price");
            String couponType = resultSet.getString("type");
            Integer discountAmount = resultSet.getInt("discount_amount");
            Double discountPercentage = resultSet.getDouble("discount_percentage");
            String expiredAt = resultSet.getString("expired_at");

            Member member = new Member(memberCouponMemberId, memberEmail, memberPassword, memberNickname);
            Coupon coupon = new Coupon(memberCouponCouponId, couponName, minOrderPrice, CouponType.valueOf(couponType),
                discountAmount, discountPercentage, maxDiscountPrice);

            return new MemberCoupon(memberCouponId, member, coupon, expiredAt);
        }, memberId);
    }

    public Coupon findByIdIfCanBeUsed(Long id, Long memberCouponId) {
        String query = "SELECT c.id, c.name, c.min_order_price, c.max_discount_price, c.type, " +
            "c.discount_amount, c.discount_percentage " +
            "FROM coupon c " +
            "INNER JOIN member_coupon mc ON c.id = mc.coupon_id " +
            "WHERE c.id = ? AND mc.id = ? AND mc.is_used = false";

        try {
            return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                Long couponId = resultSet.getLong("id");
                String couponName = resultSet.getString("name");
                Integer minOrderPrice = resultSet.getInt("min_order_price");
                Integer maxDiscountPrice = resultSet.getInt("max_discount_price");
                String couponType = resultSet.getString("type");
                Integer discountAmount = resultSet.getInt("discount_amount");
                Double discountPercentage = resultSet.getDouble("discount_percentage");

                return new Coupon(couponId, couponName, minOrderPrice, CouponType.valueOf(couponType),
                    discountAmount, discountPercentage, maxDiscountPrice);
            }, id, memberCouponId);
            //// TODO: 2023/06/01 쿠폰 사용된 경우 던질 예외 
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Coupon is already used");
        }
    }

    public void updateUsabilityById(Long memberCouponId) {
        String query = "UPDATE member_coupon SET is_used = true WHERE id = ?";

        jdbcTemplate.update(query, memberCouponId);
    }
}
