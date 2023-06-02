package cart.order.dao;

import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

  private MemberDao memberDao;

  private CouponDao couponDao;

  private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
    new OrderEntity(
        rs.getLong("id"),
        rs.getLong("member_id"),
        rs.getBigDecimal("delivery_fee")
    );

  public OrderDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDERS")
        .usingGeneratedKeyColumns("id");
  }

  public OrderDao(final JdbcTemplate jdbcTemplate, final MemberDao memberDao,
      final CouponDao couponDao) {

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

  public List<OrderEntity> findByMemberId(final long memberId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.member_id = ?";

    return jdbcTemplate.query(sql, rowMapper, memberId);
  }

  public List<Order> findByMemberId2(final long memberId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.member_id = ?";

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      final long id = rs.getLong("id");
      final Member member = memberDao.getMemberById(memberId);
      final BigDecimal deliveryFee = rs.getBigDecimal("delivery_fee");
      final long couponId = rs.getLong("coupon_id");
      final Coupon coupon = couponDao.findById(couponId);
      return new Order(id, member, new Money(deliveryFee), coupon);
    }, memberId);
  }


  public Optional<OrderEntity> findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.id = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, orderId));
    } catch (EmptyResultDataAccessException exception) {
      return Optional.empty();
    }
  }

  public Optional<Order> findByOrderId2(final Long orderId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.id = ?";

    try {
      return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final long memberId = rs.getLong("member_id");
        final Member member = memberDao.getMemberById(memberId);
        final BigDecimal deliveryFee = rs.getBigDecimal("delivery_fee");
        final long couponId = rs.getLong("coupon_id");
        final Coupon coupon = couponDao.findById(couponId);
        return Optional.of(new Order(id, member, new Money(deliveryFee), coupon));
      }, orderId);
    } catch (EmptyResultDataAccessException exception) {
      return Optional.empty();
    }
  }

  public void deleteByOrderId(final Long orderId) {
    final String sql = "DELETE FROM ORDERS O WHERE O.id = ?";

    jdbcTemplate.update(sql, orderId);
  }
}
