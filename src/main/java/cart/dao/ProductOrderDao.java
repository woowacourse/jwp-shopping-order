package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOrderDao {

  private final JdbcTemplate jdbcTemplate;

  public ProductOrderDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Long createProductOrder(final long productId, final long orderId, final int quantity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO product_order (product_id, order_id, quantity) "
              + "VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      ps.setLong(1, productId);
      ps.setLong(2, orderId);
      ps.setInt(3, quantity);

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }
}
