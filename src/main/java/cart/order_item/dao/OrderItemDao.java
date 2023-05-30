package cart.order_item.dao;

import cart.order_item.dao.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

  private final JdbcTemplate jdbcTemplate;

  private final SimpleJdbcInsert simpleJdbcInsert;

  private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) ->
      new OrderItemEntity(
          rs.getLong("id"),
          rs.getLong("order_id"),
          rs.getString("name"),
          rs.getBigDecimal("price"),
          rs.getString("image_url"),
          rs.getInt("quantity")
      );

  public OrderItemDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDER_ITEM")
        .usingGeneratedKeyColumns("id");
  }

  public void save(final List<OrderItemEntity> orderItemEntities) {
    simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(orderItemEntities));
  }

  public List<OrderItemEntity> findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDER_ITEM OI WHERE OI.order_id = ?";

    return jdbcTemplate.query(sql, rowMapper, orderId);
  }
}
