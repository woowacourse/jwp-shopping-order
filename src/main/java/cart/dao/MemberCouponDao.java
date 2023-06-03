package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

  private final JdbcTemplate jdbcTemplate;

  public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Long createMemberCoupon(final long memberId, final long couponId) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO product_order (member_id, coupon_id) "
              + "VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      ps.setLong(1, memberId);
      ps.setLong(2, couponId);
      ps.setBoolean(3, false);

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  public void use(final long memberId, final long couponId) {
    String sql = "UPDATE member_coupon SET is_used = ? WHERE member_id = ? AND coupon_id = ?";
    jdbcTemplate.update(sql, true, memberId, couponId);
  }
}
