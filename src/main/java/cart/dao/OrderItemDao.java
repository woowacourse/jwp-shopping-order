package cart.dao;

import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderItemEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private static final RowMapper<OrderItemProductDto> ORDER_PRODUCT_ROW_MAPPER = (rs, rowNum) ->
        new OrderItemProductDto(
            rs.getLong("order_item.order_id"),
            rs.getLong("order_item.id"),
            rs.getLong("product.id"),
            rs.getInt("order_item.quantity"),
            rs.getString("product.name"),
            rs.getInt("order_item.price_at_order_time"),
            rs.getString("product.image_url")
        );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void batchInsert(List<OrderItemEntity> orderItemEntities) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price_at_order_time) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
            orderItemEntities,
            orderItemEntities.size(),
            (PreparedStatement ps, OrderItemEntity entity) -> {
                ps.setLong(1, entity.getOrderId());
                ps.setLong(2, entity.getProductId());
                ps.setInt(3, entity.getQuantity());
                ps.setInt(4, entity.getPriceAtOrderTime());
            });
    }

    public List<OrderItemProductDto> findAllByOrderId(long orderId) {
        String sql =
            "SELECT order_item.order_id, order_item.id, product.id, order_item.quantity, product.name, "
                + "order_item.price_at_order_time, product.image_url FROM order_item "
                + "INNER JOIN product ON order_item.product_id = product.id "
                + "WHERE order_id = ?";
        return jdbcTemplate.query(sql, ORDER_PRODUCT_ROW_MAPPER, orderId);
    }

    public List<OrderItemProductDto> findAllByOrderIds(List<Long> orderIds) {
        String sql =
            "SELECT order_item.order_id, order_item.id, product.id, order_item.quantity, product.name, "
                + "order_item.price_at_order_time, product.image_url "
                + "FROM order_item "
                + "INNER JOIN product ON order_item.product_id = product.id "
                + "WHERE order_id IN (:orderIds)";

        return namedParameterJdbcTemplate.query(sql, Map.of("orderIds", orderIds),
            ORDER_PRODUCT_ROW_MAPPER);
    }
}
