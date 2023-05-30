package cart.dao;

import cart.dao.entity.OrderItemEntity;
import cart.domain.order.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void saveAll(final Long orderId, final List<OrderItem> orderItems) {
        List<OrderItemEntity> orderItemEntityList = orderItems.stream()
                .map(orderItem -> new OrderItemEntity(
                        orderId,
                        orderItem.getProductId(),
                        orderItem.getQuantity()
                ))
                .collect(Collectors.toList());

        final BeanPropertySqlParameterSource[] parameterSources = orderItemEntityList.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(parameterSources);
    }
}
