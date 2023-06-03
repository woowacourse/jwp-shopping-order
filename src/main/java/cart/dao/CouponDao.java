package cart.dao;

import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Product;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

  private final JdbcTemplate jdbcTemplate;

  public CouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
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
