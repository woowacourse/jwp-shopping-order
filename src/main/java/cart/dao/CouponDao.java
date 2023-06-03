package cart.dao;

import cart.domain.Amount;
import cart.domain.Coupon;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

  private final JdbcTemplate jdbcTemplate;

  public CouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Long createCoupon(final Coupon coupon) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO coupon (name, min_amount, discount_amount) VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      ps.setString(1, coupon.getName());
      ps.setInt(2, coupon.getMinAmount().getValue());
      ps.setInt(3, coupon.getDiscountAmount().getValue());

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  public Optional<Coupon> findById(final long id) {
    String sql = "SELECT * FROM coupon WHERE id = ?";
    return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
      String name = rs.getString("name");
      final Amount discountAmount = new Amount(rs.getInt("discount_amount"));
      final Amount minAmount = new Amount(rs.getInt("min_amount"));
      return new Coupon(id, name, discountAmount, minAmount);
    }).stream().findAny();
  }
}
