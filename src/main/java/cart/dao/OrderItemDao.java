package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public OrderItemEntity save(final OrderItemEntity orderItemEntity) {
        final SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderItemEntity);
        final long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new OrderItemEntity(
                savedId,
                orderItemEntity.getName(),
                orderItemEntity.getPrice(),
                orderItemEntity.getImageUrl(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getDiscountRate(),
                orderItemEntity.getOrderId()
        );
    }

    public void batchSave(final List<OrderItemEntity> orderItemEntities) {
        final BeanPropertySqlParameterSource[] parameterSources = orderItemEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(parameterSources);
    }
}
