package cart.dao;

import cart.domain.Amount;
import cart.domain.Product;
import cart.domain.ProductOrder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
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

  public Long createProductOrder(final ProductOrder productOrder, final long orderId) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO product_order (product_id, order_id, quantity) "
              + "VALUES (?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      ps.setLong(1, productOrder.getId());
      ps.setLong(2, orderId);
      ps.setInt(3, productOrder.getQuantity());

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  public List<ProductOrder> findByOrderId(Long orderId) {
    String sql = "SELECT product.id, product.name, product.price, product.image_url, "
        + "product_order.id, product_order.quantity " +
        "FROM product_order " +
        "INNER JOIN product ON product.id = product_order.product_id " +
        "WHERE product_order.order_id = ?";

    return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) -> {
      Long productId = rs.getLong("product.id");
      String name = rs.getString("name");
      int price = rs.getInt("price");
      String imageUrl = rs.getString("image_url");
      Long productOrderId = rs.getLong("product_order.id");
      final int quantity = rs.getInt("quantity");
      final Product product = new Product(productId, name, new Amount(price), imageUrl);
      return new ProductOrder(productOrderId, product, quantity);
    });
  }
}
