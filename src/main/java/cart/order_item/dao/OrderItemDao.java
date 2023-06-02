package cart.order_item.dao;

import cart.order.dao.OrderDao;
import cart.order.domain.Order;
import cart.order_item.dao.entity.OrderItemEntity;
import cart.order_item.domain.OrderItem;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
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

  private final OrderDao orderDao;

  public OrderItemDao(final JdbcTemplate jdbcTemplate, final OrderDao orderDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDER_ITEM")
        .usingGeneratedKeyColumns("id");
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    this.orderDao = orderDao;
  }

  public void save(final List<OrderItemEntity> orderItemEntities) {
    simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(orderItemEntities));
  }

  public List<OrderItem> findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDER_ITEM OI WHERE OI.order_id = ?";

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      final long id = rs.getLong("id");
      final Order order = orderDao.findByOrderId(orderId);
      final String name = rs.getString("name");
      final BigDecimal price = rs.getBigDecimal("price");
      final String imageUrl = rs.getString("image_url");
      final int quantity = rs.getInt("quantity");
      return new OrderItem(id, order, name, new Money(price), imageUrl, quantity);
    }, orderId);
  }

  public void deleteBatch(final List<Long> orderItemIds) {
    final String sql = "DELETE FROM ORDER_ITEM OI WHERE OI.id IN (:ids)";

    final MapSqlParameterSource parameterSource =
        new MapSqlParameterSource().addValue("ids", orderItemIds);

    namedParameterJdbcTemplate.update(sql, parameterSource);
  }
}
