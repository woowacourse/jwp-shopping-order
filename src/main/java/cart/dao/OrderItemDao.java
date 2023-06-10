package cart.dao;

import cart.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) -> new OrderItemEntity(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url"),
            rs.getInt("quantity")
    );

    public void saveAll(final List<OrderItemEntity> orderItemEntities) {
        final String sql = "INSERT INTO order_item (order_id, product_id, name, price, image_url, quantity) VALUES (?,?,?,?,?,?) ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                OrderItemEntity orderItemEntity = orderItemEntities.get(i);
                ps.setLong(1, orderItemEntity.getOrderId());
                ps.setLong(2, orderItemEntity.getProductId());
                ps.setString(3, orderItemEntity.getName());
                ps.setInt(4, orderItemEntity.getPrice());
                ps.setString(5, orderItemEntity.getImageUrl());
                ps.setInt(6, orderItemEntity.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return orderItemEntities.size();
            }
        });
    }

    public List<OrderItemEntity> findByOrderIds(final List<Long> orderIds) {
        final String sql = "SELECT * FROM order_item WHERE order_id IN (%s) ";

        String inSql = String.join(",", Collections.nCopies(orderIds.size(), "?"));
        return jdbcTemplate.query(
                String.format(sql, inSql),
                orderIds.toArray(),
                rowMapper
        );
    }

    public List<OrderItemEntity> finByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_item WHERE order_id = ?";

        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

    public void deleteByOrderId(final Long id) {
        final String sql = "DELETE FROM order_item WHERE order_id = ? ";

        jdbcTemplate.update(sql, id);
    }
}
