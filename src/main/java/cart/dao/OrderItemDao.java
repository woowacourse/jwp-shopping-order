package cart.dao;

import cart.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<OrderItemEntity> rowMapper = (resultSet, rowNum) -> new OrderItemEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getLong("price"),
            resultSet.getString("image_url"),
            resultSet.getLong("quantity"),
            resultSet.getLong("product_id"),
            resultSet.getLong("orders_id")
    );

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

//    public void saveAll(final Long orderId, final List<OrderItem> orderItems) {
//        List<OrderItemEntity> orderItemEntityList = orderItems.stream()
//                .map(orderItem -> new OrderItemEntity(
//                        orderId,
//                        orderItem.getProductId(),
//                        orderItem.getQuantity()
//                ))
//                .collect(Collectors.toList());
//
//        final BeanPropertySqlParameterSource[] parameterSources = orderItemEntityList.stream()
//                .map(BeanPropertySqlParameterSource::new)
//                .toArray(BeanPropertySqlParameterSource[]::new);
//        simpleJdbcInsert.executeBatch(parameterSources);
//    }

    public List<OrderItemEntity> findAllByOrderIds(final List<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "SELECT * FROM order_item WHERE order_id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", orderIds);
        return namedJdbcTemplate.query(sql, parameters, rowMapper);
    }
}
