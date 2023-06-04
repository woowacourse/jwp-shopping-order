package cart.dao;

import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.ProductOrder;
import cart.domain.Products;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    String s = "SELECT order.id, "
        + "product.id, product.name, product.price, product_image_url "
        + "FROM `order` "
        + "INNER JOIN product_order ON product_order.order_id = order.id "
        + "INNER JOIN product ON product.id = product_order.product_id "
        + "WHERE order.member_id = ?";

    final List<OrderProductJoinDto> orderProductJoinDtos = jdbcTemplate.query(s, new Object[]{memberId}, (rs, rowNum) -> {
      Long orderId = rs.getLong("order.id");
      Long productId = rs.getLong("product.id");
      String name = rs.getString("name");
      int price = rs.getInt("price");
      String imageUrl = rs.getString("image_url");
      return new OrderProductJoinDto(orderId, productId, name, price, imageUrl);
    });

    final HashMap<Long, List<Product>> productsByOrderId = new HashMap<>();
    for (OrderProductJoinDto orderProductJoinDto : orderProductJoinDtos) {
      final Long orderId = orderProductJoinDto.orderId;
      final List<Product> products = productsByOrderId.getOrDefault(orderId, new ArrayList<>());
      products.add(
          new Product(orderProductJoinDto.productId, orderProductJoinDto.name, new Amount(orderProductJoinDto.price),
              orderProductJoinDto.imageUrl));
      productsByOrderId.put(orderId, products);
    }

    String sql = "SELECT order.id, order.devliery_amount, order.address, "
        + "product.id, product.name, product.image_url, product.price, "
        + "member.id, member.email, member.password, "
        + "product_order.id, product_order.quantity "
        + "coupon.id, coupon.name, coupon.min_amount, coupon.discount_amount" +
        "FROM `order` " +
        "INNER JOIN product_order ON product_order.order_id = order.id " +
        "INNER JOIN product ON product_order.product_id = product.id " +
        "WHERE order.member_id = ?";

    return jdbcTemplate.query(sql, new Object[]{memberId}, getProductOrderRowMapper(productsByOrderId));
  }

  public List<ProductOrder> findOrderByOrderId(final Long memberId, final Long orderId) {
    String s = "SELECT product.id, product.name, product.price, product_image_url "
        + "FROM `order` "
        + "INNER JOIN product_order ON product_order.order_id = order.id "
        + "INNER JOIN product ON product.id = product_order.product_id "
        + "WHERE order.id = ?";

    final List<Product> products = jdbcTemplate.query(s, new Object[]{orderId}, (rs, rowNum) -> {
      Long productId = rs.getLong("product.id");
      String name = rs.getString("name");
      int price = rs.getInt("price");
      String imageUrl = rs.getString("image_url");
      return new Product(productId, name, new Amount(price), imageUrl);
    });

    final HashMap<Long, List<Product>> productsByOrderId = new HashMap<>();
    productsByOrderId.put(orderId, products);

    String sql = "SELECT order.id, order.devliery_amount, order.address, "
        + "product.id, product.name, product.image_url, product.price, "
        + "member.id, member.email, member.password, "
        + "product_order.id, product_order.quantity "
        + "coupon.id, coupon.name, coupon.min_amount, coupon.discount_amount" +
        "FROM `order` " +
        "INNER JOIN member ON member.id = order.member_id " +
        "INNER JOIN product_order ON product_order.order_id = order.id " +
        "INNER JOIN product ON product_order.product_id = product.id " +
        "INNER JOIN coupon ON order.coupon_id = coupon = coupon.id " +
        "WHERE order.id = ? AND member.id = ?";

    return jdbcTemplate.query(sql, new Object[]{orderId, memberId}, getProductOrderRowMapper(productsByOrderId));
  }

  private RowMapper<ProductOrder> getProductOrderRowMapper(final HashMap<Long, List<Product>> productsByOrderId) {
    return (rs, rowNum) -> {
      Long orderId = rs.getLong("order.id");
      String address = rs.getString("address");
      int deliveryAmount = rs.getInt("delivery_amount");
      Long productId = rs.getLong("product.id");
      String productName = rs.getString("product.name");
      int price = rs.getInt("price");
      String imageUrl = rs.getString("image_url");
      Long productOrderId = rs.getLong("product_order.id");
      int quantity = rs.getInt("quantity");
      Long memberId = rs.getLong("member.id");
      String email = rs.getString("email");
      String password = rs.getString("password");
      Long couponId = rs.getLong("coupon.id");
      String couponName = rs.getString("coupon.name");
      int minAmount = rs.getInt("min_amount");
      int discountAmount = rs.getInt("discount_amount");
      final Product product = new Product(productId, productName, new Amount(price), imageUrl);
      final Member member = new Member(memberId, email, password);
      final Coupon coupon = new Coupon(couponId, couponName, new Amount(discountAmount), new Amount(minAmount));
      final Order order = new Order(orderId, member,
          new Products(productsByOrderId.getOrDefault(orderId, new ArrayList<>())), coupon, new Amount(deliveryAmount),
          address);
      return new ProductOrder(productOrderId, product, order, quantity);
    };
  }

  class OrderProductJoinDto {

    private Long orderId;
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;

    public OrderProductJoinDto(Long orderId, Long productId, String name, int price, String imageUrl) {
      this.orderId = orderId;
      this.productId = productId;
      this.name = name;
      this.price = price;
      this.imageUrl = imageUrl;
    }

    public Long getOrderId() {
      return orderId;
    }

    public Long getProductId() {
      return productId;
    }

    public String getName() {
      return name;
    }

    public int getPrice() {
      return price;
    }

    public String getImageUrl() {
      return imageUrl;
    }
  }
}
