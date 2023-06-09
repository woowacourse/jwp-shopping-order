package cart.dao;

import cart.dao.entity.OrderDetailEntity;
import cart.dao.entity.OrderEntity;
import cart.domain.Order;
import cart.domain.OrderDetail;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long saveOrder(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id",order.getMember().getId());
        params.put("payment",order.getPayment().getPayment().longValue());
        params.put("discount_point",order.getPoint().getPoint().longValue());

        Long orderId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        final String sql = "INSERT INTO order_detail(order_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final OrderDetail orderDetail = order.getOrderDetails().get(i);
                ps.setLong(1, orderId);
                ps.setLong(2, orderDetail.getProduct().getId());
                ps.setLong(3, orderDetail.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return order.getOrderDetails().size();
            }
        });
        return orderId;
    }

    public List<OrderEntity> findOrdersByMemberId(final Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("payment"),
                rs.getLong("discount_point")
        ), memberId);
    }

    public List<OrderDetailEntity> findOrderDetailsByOrderId(final Long orderId) {
        String sql = "SELECT * FROM order_detail WHERE order_id =?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> new OrderDetailEntity(
                rs.getLong("id"),
                rs.getLong("order_id"),
                rs.getLong("product_id"),
                rs.getLong("quantity")
        ),orderId);
    }

    public OrderEntity findOrderById(final Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{orderId},(rs, rowNum) -> new OrderEntity(
                orderId,
                rs.getLong("member_id"),
                rs.getLong("payment"),
                rs.getLong("discount_point")

        ));
    }
}
