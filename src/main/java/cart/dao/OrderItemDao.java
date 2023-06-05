package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) -> new OrderItemEntity(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getString("product_name"),
            rs.getInt("product_price"),
            rs.getString("product_image_url"),
            rs.getInt("quantity")
    );

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(List<OrderItemEntity> orderItemEntities) {
        String sql = "INSERT INTO tb_order_item(order_id, product_id, product_name, product_price, product_image_url, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                orderItemEntities,
                orderItemEntities.size(),
                (ps, orderItemEntity) -> {
                    ps.setLong(1, orderItemEntity.getOrderId());
                    ps.setLong(2, orderItemEntity.getProductId());
                    ps.setString(3, orderItemEntity.getProductName());
                    ps.setInt(4, orderItemEntity.getProductPrice());
                    ps.setString(5, orderItemEntity.getProductImageUrl());
                    ps.setInt(6, orderItemEntity.getQuantity());
                });
    }

    public List<OrderItemEntity> findByOrderId(Long orderId) {
        String sql = "SELECT id, order_id, product_id, product_name, product_price, product_image_url, quantity "
                + "FROM tb_order_item "
                + "WHERE order_id = ?";

        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
