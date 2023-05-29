package cart.order_item.dao;

import cart.order_item.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

  private final JdbcTemplate jdbcTemplate;

  private final SimpleJdbcInsert simpleJdbcInsert;

  public OrderItemDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDER_ITEM")
        .usingGeneratedKeyColumns("id");
  }

  public void save(final OrderItemEntity orderItemEntity) {
    simpleJdbcInsert.execute(new BeanPropertySqlParameterSource(orderItemEntity));
  }
}
