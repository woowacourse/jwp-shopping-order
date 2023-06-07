package cart.dao;

import cart.entity.OrderItemEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) -> new OrderItemEntity(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getLong("price_at_order"),
            rs.getInt("quantity")
    );

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                Objects.requireNonNull(jdbcTemplate.getDataSource())
        );
    }

    public void batchInsert(final List<OrderItemEntity> orderItems) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price_at_order) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                orderItems,
                orderItems.size(),
                (PreparedStatement ps, OrderItemEntity entity) -> {
                    ps.setLong(1, entity.getOrderId());
                    ps.setLong(2, entity.getProductId());
                    ps.setInt(3, entity.getQuantity());
                    ps.setLong(4, entity.getPriceAtOrder());
                });
    }

    public List<OrderItemEntity> findByOrderId(final Long orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";

        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

    public Map<Long, List<OrderItemEntity>> findGroupByOrderId(final List<Long> orderIds) {
        String sql = "SELECT * FROM order_item WHERE order_id IN (:orderIds)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orderIds", orderIds);

        final List<OrderItemEntity> orderItemEntities = namedParameterJdbcTemplate.query(sql, params, rowMapper);

        return orderItemEntities.stream().collect(Collectors.groupingBy(
                OrderItemEntity::getOrderId,
                Collectors.toList()
        ));
    }
}
