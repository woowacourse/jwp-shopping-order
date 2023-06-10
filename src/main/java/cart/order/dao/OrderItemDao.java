package cart.order.dao;

import cart.order.dao.entity.OrderItemEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public OrderItemDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDER_ITEM")
        .usingGeneratedKeyColumns("id");
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  public void save(final Order order) {
    final List<OrderItemEntity> orderItemEntities = OrderItemEntity.makeOrderItemEntities(order);
    simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(orderItemEntities));
  }

  public List<OrderItem> findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDER_ITEM OI WHERE OI.order_id = ?";

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      final long id = rs.getLong("id");
      final String name = rs.getString("name");
      final BigDecimal price = rs.getBigDecimal("price");
      final String imageUrl = rs.getString("image_url");
      final int quantity = rs.getInt("quantity");
      return new OrderItem(id, name, new Money(price), imageUrl, quantity);
    }, orderId);
  }

  public void deleteBatch(final Order order) {
    final String sql = "DELETE FROM ORDER_ITEM OI WHERE OI.id IN (:ids)";

    final List<Long> deletedOrderItemIds = findByOrderId(order.getId())
        .stream()
        .map(OrderItem::getId)
        .collect(Collectors.toList());

    final MapSqlParameterSource parameterSource =
        new MapSqlParameterSource().addValue("ids", deletedOrderItemIds);

    namedParameterJdbcTemplate.update(sql, parameterSource);
  }
}
