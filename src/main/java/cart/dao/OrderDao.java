package cart.dao;

import static java.sql.Types.NULL;

import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
public class OrderDao {

  private final JdbcTemplate jdbcTemplate;
  private final ProductOrderDao productOrderDao;

  public OrderDao(final JdbcTemplate jdbcTemplate, ProductOrderDao productOrderDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.productOrderDao = productOrderDao;
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
      setCouponId(order, ps);
      ps.setString(3, order.getAddress());
      ps.setInt(4, order.getDeliveryAmount().getValue());

      return ps;
    }, keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  private static void setCouponId(Order order, PreparedStatement ps) throws SQLException {
    if (order.getCoupon().getId() != null) {
      ps.setLong(2, order.getCoupon().getId());
      return;
    }
    ps.setLong(2, NULL);
  }

  public List<Order> findAllOrdersByMemberId(Long memberId) {
    String sql = "SELECT `order`.id, `order`.delivery_amount, `order`.address, "
        + "member.id, member.email, member.password, "
        + "coupon.id, coupon.name, coupon.min_amount, coupon.discount_amount " +
        "FROM `order` " +
        "INNER JOIN member ON member.id = `order`.member_id " +
        "INNER JOIN coupon ON `order`.coupon_id = coupon.id " +
        "WHERE `order`.member_id = ?";

    return jdbcTemplate.query(sql, new Object[]{memberId}, getOrderRowMapper());
  }

  public Optional<Order> find(final Long orderId) {
    String sql = "SELECT * FROM `order` where = ?";

    return jdbcTemplate.query(sql, new Object[]{orderId}, getOrderRowMapper()).stream().findAny();
  }

  public Optional<Order> findOrderByOrderId(final Long orderId) {
    String sql = "SELECT `order`.id, `order`.delivery_amount, `order`.address, "
        + "member.id, member.email, member.password, "
        + "coupon.id, coupon.name, coupon.min_amount, coupon.discount_amount " +
        "FROM `order` " +
        "INNER JOIN member ON member.id = `order`.member_id " +
        "INNER JOIN coupon ON `order`.coupon_id = coupon.id " +
        "WHERE `order`.id = ?";

    return jdbcTemplate.query(sql, new Object[]{orderId}, getOrderRowMapper()).stream().findAny();
  }

  private RowMapper<Order> getOrderRowMapper() {
    return (rs, rowNum) -> {
      Long orderId = rs.getLong("order.id");
      int deliveryAmount = rs.getInt("delivery_amount");
      String address = rs.getString("address");
      Long memberId = rs.getLong("member.id");
      String email = rs.getString("email");
      String password = rs.getString("password");
      Long couponId = rs.getLong("coupon.id");
      String couponName = rs.getString("coupon.name");
      int minAmount = rs.getInt("min_amount");
      int discountAmount = rs.getInt("discount_amount");
      final Member member = new Member(memberId, email, password);
      final Coupon coupon = new Coupon(couponId, couponName, discountAmount, minAmount);
      return new Order(orderId, member, productOrderDao.findByOrderId(orderId), coupon,
          new Amount(deliveryAmount), address);
    };
  }
}
