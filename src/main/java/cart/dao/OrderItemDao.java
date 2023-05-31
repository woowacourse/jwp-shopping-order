package cart.dao;

import cart.dao.entity.OrderItemEntity;
import cart.domain.OrderItem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
}
