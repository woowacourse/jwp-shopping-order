package cart.dao;

import cart.domain.Order;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

  private final JdbcTemplate jdbcTemplate;

  public OrderDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Long createOrder(final Order order) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO `order` (member_id, coupon_id, address, delivery_amount) "
              + "VALUES (?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      ps.setLong(1, order.getMember().getId());
      ps.setLong(2, order.getCoupon().getId());
      ps.setString(3, order.getAddress());
      ps.setInt(4, order.getDeliveryAmount().getValue());

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }
}
