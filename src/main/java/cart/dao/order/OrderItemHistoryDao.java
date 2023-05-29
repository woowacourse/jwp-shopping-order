package cart.dao.order;

import cart.entity.order.OrderItemHistoryEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemHistoryDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderItemHistoryDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<OrderItemHistoryEntity> rowMapper = (rs, rowNum) ->
            new OrderItemHistoryEntity(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getString("product_name"),
                    rs.getString("image_url"),
                    rs.getInt("price"),
                    rs.getInt("quantity"),
                    rs.getLong("order_table_id")
            );


    public void saveAll(final List<OrderItemHistoryEntity> historyEntities) {
        String sql = "INSERT INTO order_item_history (product_id, product_name, image_url, price, quantity, order_table_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderItemHistoryEntity entity = historyEntities.get(i);
                ps.setLong(1, entity.getProductId());
                ps.setString(2, entity.getProductName());
                ps.setString(3, entity.getImgUrl());
                ps.setInt(4, entity.getPrice());
                ps.setInt(5, entity.getQuantity());
                ps.setLong(6, entity.getOrderTableId());
            }

            @Override
            public int getBatchSize() {
                return historyEntities.size();
            }
        });
    }

    public List<OrderItemHistoryEntity> findAllByOrderId(final Long orderId) {
        String sql = "SELECT * FROM order_item_history WHERE order_table_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
