package cart.dao;

import cart.entity.OrderItemEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) ->
            new OrderItemEntity(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("shopping_order_id"),
                    rs.getString("product_name_at_order"),
                    rs.getInt("product_price_at_order"),
                    rs.getString("product_image_url_at_order"),
                    rs.getInt("quantity")
            );

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchSave(List<OrderItemEntity> orderItemEntities) {
        final String sql = "INSERT INTO ordered_item "
                + "(`product_id`, `shopping_order_id`, `product_name_at_order`, `product_price_at_order`, `product_image_url_at_order`, `quantity`)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, orderItemEntities.get(i).getProductId());
                ps.setLong(2, orderItemEntities.get(i).getShoppingOrderId());
                ps.setString(3, orderItemEntities.get(i).getProductNameAtOrder());
                ps.setInt(4, orderItemEntities.get(i).getProductPriceAtOrder());
                ps.setString(5, orderItemEntities.get(i).getProductImageUrlAtOrder());
                ps.setInt(6, orderItemEntities.get(i).getQuantity());
            }

            @Override
            public int getBatchSize() {
                return orderItemEntities.size();
            }
        });
    }

    public List<OrderItemEntity> findByOrderId(Long orderId) {
        final String sql = "SELECT * FROM ordered_item WHERE shopping_order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
