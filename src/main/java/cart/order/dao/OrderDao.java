package cart.order.dao;

import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.exception.CouponException;
import cart.coupon.exception.CouponExceptionType;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.order.domain.OrderedItems;
import cart.order.exception.enum_exception.OrderException;
import cart.order.exception.enum_exception.OrderExceptionType;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final MemberDao memberDao;
  private final CouponDao couponDao;
  private final OrderItemDao orderItemDao;

  public OrderDao(
      final JdbcTemplate jdbcTemplate,
      final MemberDao memberDao,
      final CouponDao couponDao,
      final OrderItemDao orderItemDao
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDERS")
        .usingGeneratedKeyColumns("id");
    this.memberDao = memberDao;
    this.couponDao = couponDao;
    this.orderItemDao = orderItemDao;
  }

  public Long save(final OrderEntity orderEntity) {
    return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderEntity))
        .longValue();
  }

  public List<Order> findByMemberId(final long memberId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.member_id = ?";

    return jdbcTemplate.query(sql, getRowMapper(), memberId);
  }

  private RowMapper<Order> getRowMapper() {
    return (rs, rowNum) -> {
      final long id = rs.getLong("id");
      final long memberId = rs.getLong("member_id");
      final Member member = memberDao.getMemberById(memberId);
      final BigDecimal deliveryFee = rs.getBigDecimal("delivery_fee");
      final Long couponId = rs.getLong("coupon_id");
      final Coupon coupon = couponDao.findById(couponId)
          .orElseThrow(() -> new CouponException(CouponExceptionType.NOT_FOUNT_COUPON));
      final Timestamp createdAt = rs.getTimestamp("created_at");
      final Instant instant = createdAt.toInstant();
      final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
      final String orderStatus = rs.getString("order_status");
      final OrderedItems orderedItems = OrderedItems.createdFromLookUp(
          orderItemDao.findByOrderId(id)
      );
      return new Order(
          id, member,
          new Money(deliveryFee), coupon,
          OrderStatus.findOrderStatus(orderStatus), zonedDateTime,
          orderedItems
      );
    };
  }

  public Order findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.id = ?";

    try {
      return jdbcTemplate.queryForObject(sql, getRowMapper(), orderId);
    } catch (EmptyResultDataAccessException exception) {
      throw new OrderException(OrderExceptionType.CAN_NOT_FOUND_ORDER);
    }
  }

  public void deleteByOrderId(final Long orderId) {
    final String sql = "DELETE FROM ORDERS O WHERE O.id = ?";

    jdbcTemplate.update(sql, orderId);
  }

  public void updateByOrderId(final Long orderId, final String orderStatus) {
    final String sql = "UPDATE ORDERS O SET O.order_status = ? WHERE O.id = ?";

    jdbcTemplate.update(sql, orderStatus, orderId);
  }
}
