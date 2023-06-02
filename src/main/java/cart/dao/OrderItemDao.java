package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderItemEntity> orderItemRowMapper = (rs, rowNum) ->
            new OrderItemEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getInt("quantity"),
                    rs.getInt("discount_rate"),
                    rs.getLong("order_id")
            );

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

    public List<OrderItemEntity> findAllByOrderId(final Long orderId) {
        final String sql = "select * from order_item where order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }

    public List<OrderItemEntity> findAllOrderItemsByOrderIdAndMemberId(final Long orderId, final Long memberId) {
        final String sql = "select * from order_item where id = ? and (select member_id from orders where id = ?) = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId, orderId, memberId);
    }
}
