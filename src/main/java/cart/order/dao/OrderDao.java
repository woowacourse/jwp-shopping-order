package cart.order.dao;

import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.exception.NotFoundCouponException;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.order.exception.NotFoundOrderException;
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

  public OrderDao(
      final JdbcTemplate jdbcTemplate,
      final MemberDao memberDao,
      final CouponDao couponDao
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDERS")
        .usingGeneratedKeyColumns("id");
    this.memberDao = memberDao;
    this.couponDao = couponDao;
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
          .orElseThrow(() -> new NotFoundCouponException("해당 쿠폰은 존재하지 않습니다"));
      final Timestamp createdAt = rs.getTimestamp("created_at");
      final Instant instant = createdAt.toInstant();
      final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
      final String orderStatus = rs.getString("order_status");
      return new Order(
          id, member,
          new Money(deliveryFee), coupon,
          OrderStatus.findOrderStatus(orderStatus), zonedDateTime
      );
    };
  }

  public Order findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.id = ?";

    try {
      return jdbcTemplate.queryForObject(sql, getRowMapper(), orderId);
    } catch (EmptyResultDataAccessException exception) {
      throw new NotFoundOrderException("해당 주문은 존재하지 않습니다.");
    }
  }

  public void deleteByOrderId(final Long orderId) {
    final String sql = "DELETE FROM ORDERS O WHERE O.id = ?";

    jdbcTemplate.update(sql, orderId);
  }

  public void updateOrderByOrderId(final Long orderId, final String orderStatus) {
    final String sql = "UPDATE ORDERS O SET O.order_status = ? WHERE O.id = ?";

    jdbcTemplate.update(sql, orderStatus, orderId);
  }
}
