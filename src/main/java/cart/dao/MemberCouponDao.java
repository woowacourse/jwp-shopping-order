package cart.dao;

import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

  private final JdbcTemplate jdbcTemplate;

  public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Long save(final Long memberId, final Long couponId) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO member_coupon (is_used, member_id, coupon_id) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      ps.setBoolean(1, false);
      ps.setLong(2, memberId);
      ps.setLong(3, couponId);

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  public Optional<MemberCoupon> findMemberCouponById(final Long memberCouponId) {
    String sql =
        "SELECT member.id, member.email, member.password, "
            + "member_coupon.id, member_coupon.coupon_id, member_coupon.is_used, "
            + "coupon.name, coupon.min_amount, coupon.discount_amount "
            + "FROM member_coupon "
            + "INNER JOIN member ON member_coupon.member_id = member.id "
            + "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id "
            + "WHERE member_coupon.id = ?";

    return jdbcTemplate.query(sql, new Object[]{memberCouponId}, getMemberCouponRowMapper()).stream().findAny();
  }

  public void use(final long memberId, final long couponId) {
    String sql = "UPDATE member_coupon SET is_used = ? WHERE member_id = ? AND coupon_id = ?";
    jdbcTemplate.update(sql, true, memberId, couponId);
  }

  public List<MemberCoupon> findMemberCouponsByMemberId(final Long memberId) {
    String sql =
        "SELECT member.id, member.email, member.password, "
            + "member_coupon.id, member_coupon.coupon_id, member_coupon.is_used, "
            + "coupon.name, coupon.min_amount, coupon.discount_amount "
            + "FROM member_coupon "
            + "INNER JOIN member ON member_coupon.member_id = member.id "
            + "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id "
            + "WHERE member_coupon.member_id = ?";

    return jdbcTemplate.query(sql, new Object[]{memberId}, getMemberCouponRowMapper());
  }

  private static RowMapper<MemberCoupon> getMemberCouponRowMapper() {
    return (rs, rowNum) -> {
      final long findMemberId = rs.getLong("member.id");
      final String email = rs.getString("email");
      final String password = rs.getString("password");

      final long memberCouponId = rs.getLong("member_coupon.id");
      Long couponId = rs.getLong("member_coupon.coupon_id");
      final boolean isUsed = rs.getBoolean("is_used");

      String name = rs.getString("name");
      final int minAmount = rs.getInt("min_amount");
      final int discountAmount = rs.getInt("discount_amount");

      final Member member = new Member(findMemberId, email, password);
      final Coupon coupon = new Coupon(couponId, name, new Amount(discountAmount), new Amount(minAmount));
      return new MemberCoupon(memberCouponId, member, coupon, isUsed);
    };
  }

  public List<MemberCoupon> findAvailableCouponsByMemberIdAndTotalAmount(final Long memberId, final int totalAmount) {
    String sql =
        "SELECT member.id, member.email, member.password, "
            + "member_coupon.id, member_coupon.coupon_id, member_coupon.is_used, "
            + "coupon.name, coupon.min_amount, coupon.discount_amount "
            + "FROM member_coupon "
            + "INNER JOIN member ON member_coupon.member_id = member.id "
            + "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id and coupon.min_amount <= ?"
            + "WHERE member_coupon.member_id = ? and member_coupon.is_used = ?";

    return jdbcTemplate.query(sql, new Object[]{totalAmount, memberId, false}, getMemberCouponRowMapper());
  }

  public boolean existsByMemberIdAndCouponId(Long memberId, Long couponId) {
    String sql =
        "SELECT member.id, member.email, member.password, "
            + "member_coupon.id, member_coupon.coupon_id, member_coupon.is_used, "
            + "coupon.name, coupon.min_amount, coupon.discount_amount "
            + "FROM member_coupon "
            + "INNER JOIN member ON member_coupon.member_id = member.id "
            + "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id "
            + "WHERE member_coupon.member_id = ? AND member_coupon.coupon_id = ?";
    final List<MemberCoupon> memberCoupon = jdbcTemplate.query(sql, new Object[]{memberId, couponId},
        getMemberCouponRowMapper());
    return !memberCoupon.isEmpty();
  }
}
