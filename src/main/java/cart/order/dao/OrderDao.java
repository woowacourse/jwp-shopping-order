package cart.order.dao;

import cart.order.dao.entity.OrderEntity;
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

  public Long save(final OrderEntity orderEntity) {
    return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderEntity))
        .longValue();
  }

  public List<OrderEntity> findByMemberId(final long memberId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.member_id = ?";

    return jdbcTemplate.query(sql, rowMapper, memberId);
  }

  public Optional<OrderEntity> findByOrderId(final Long orderId) {
    final String sql = "SELECT * FROM ORDERS O WHERE O.id = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, orderId));
    } catch (EmptyResultDataAccessException exception) {
      return Optional.empty();
    }
  }

  public void deleteByOrderId(final Long orderId) {
    final String sql = "DELETE FROM ORDERS O WHERE O.id = ?";

    jdbcTemplate.update(sql, orderId);
  }
}
