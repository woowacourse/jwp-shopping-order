package cart.dao;

import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

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
            Timestamp expiredAt = resultSet.getTimestamp("expired_at");

            Member member = new Member(memberCouponMemberId, memberEmail, memberPassword, memberNickname);
            Coupon coupon = new Coupon(memberCouponCouponId, couponName, minOrderPrice, CouponType.valueOf(couponType),
                discountAmount, discountPercentage, maxDiscountPrice);

            return new MemberCoupon(memberCouponId, member, coupon, expiredAt);
        }, memberId);
    }

    public MemberCoupon findByIdIfCanBeUsed(Long memberId, Long memberCouponId) {
        String query = "SELECT c.id, c.name, c.min_order_price, c.max_discount_price, c.type, " +
            "c.discount_amount, c.discount_percentage, "+
            "mc.coupon_id, mc.expired_at, " +
            "m.email, m.password, m.nickname " +
            "FROM member_coupon mc " +
            "INNER JOIN coupon c ON mc.coupon_id = c.id " +
            "INNER JOIN member m ON mc.member_id = m.id" +
            "WHERE m.id = ? AND mc.id = ? AND mc.is_used = false";

        try {
            return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
                Long couponId = resultSet.getLong("c.id");
                String couponName = resultSet.getString("c.name");
                Integer minOrderPrice = resultSet.getInt("c.min_order_price");
                Integer maxDiscountPrice = resultSet.getInt("c.max_discount_price");
                CouponType couponType = CouponType.valueOf(resultSet.getString("c.type"));
                Integer discountAmount = resultSet.getInt("c.discount_amount");
                Double discountPercentage = resultSet.getDouble("c.discount_percentage");

                Coupon coupon = new Coupon(couponId, couponName, minOrderPrice, couponType, discountAmount,
                    discountPercentage,
                    maxDiscountPrice);

                String email = resultSet.getString("m.email");
                String password = resultSet.getString("m.password");
                String nickname = resultSet.getString("m.nickname");

                Member member = new Member(memberId, email, password, nickname);

                Timestamp expiredAt = resultSet.getTimestamp("mc.expired_at");

                return new MemberCoupon(memberCouponId, member, coupon, expiredAt);
            }, memberId, memberCouponId);
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequestException(ExceptionType.COUPON_UNVAILABLE);
        }
    }

    public void updateUsabilityById(Long memberCouponId) {
        String query = "UPDATE member_coupon SET is_used = true WHERE id = ?";

        jdbcTemplate.update(query, memberCouponId);
    }

    public Long save(MemberCoupon memberCoupon) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO member_coupon (member_id, coupon_id, expired_at) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, memberCoupon.getMember().getId());
            ps.setLong(2, memberCoupon.getCoupon().getId());
            ps.setTimestamp(3, Timestamp.valueOf(memberCoupon.getExpiredAt()));

            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return Objects.requireNonNull(key).longValue();
    }
}
