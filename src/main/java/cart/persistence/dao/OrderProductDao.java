package cart.persistence.dao;

import cart.persistence.entity.OrderProductEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(final List<OrderProductEntity> orderProductEntities) {
        final String sql = "INSERT INTO order_product(order_id, product_id, ordered_product_price) "
            + "VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, orderProductEntities.get(i).getOrderId());
                ps.setLong(2, orderProductEntities.get(i).getProductId());
                ps.setInt(3, orderProductEntities.get(i).getOrderProductPrice());
            }

            @Override
            public int getBatchSize() {
                return orderProductEntities.size();
            }
        });
    }
}
