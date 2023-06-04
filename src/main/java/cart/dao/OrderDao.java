package cart.dao;

import cart.domain.Amount;
import cart.domain.Order;
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

  public List<ProductOrder> findAllOrdersByMemberId(Long memberId) {
    String sql = "SELECT order.id, "
        + "product.id, product.name, product.image_url, product.price "
        + "product_order.id, product_order.quantity" +
        "FROM `order` " +
        "INNER JOIN product_order ON product_order.order_id = order.id " +
        "INNER JOIN product ON product_order.product_id = product.id " +
        "WHERE order.member_id = ?";

    return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
      Long orderId = rs.getLong("order.id");
      Long productId = rs.getLong("product.id");
      String name = rs.getString("name");
      int price = rs.getInt("price");
      String imageUrl = rs.getString("image_url");
      Long productOrderId = rs.getLong("product_order.id");
      int quantity = rs.getInt("quantity");
      Product product = new Product(productId, name, new Amount(price), imageUrl);
      return new ProductOrder(productOrderId, product, orderId, quantity);
    });
  }
}
