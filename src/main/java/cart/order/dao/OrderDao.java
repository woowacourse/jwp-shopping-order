package cart.order.dao;

import cart.order.dao.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

  private final JdbcTemplate jdbcTemplate;

  private final SimpleJdbcInsert simpleJdbcInsert;

  public OrderDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("ORDERS")
        .usingGeneratedKeyColumns("id");
  }

  public Long save(final OrderEntity orderEntity) {
    return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderEntity))
        .longValue();
  }
}
