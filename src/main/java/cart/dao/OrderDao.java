package cart.dao;

import cart.dao.entity.OrderDetailEntity;
import cart.dao.entity.OrderEntity;
import cart.domain.Order;
import cart.domain.OrderDetail;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final String sql = "INSERT INTO orders (member_id, payment, discount_point) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, order.getPayment().getPayment().longValue());
            ps.setLong(3, order.getPoint().getPoint().longValue());

            return ps;
        }, keyHolder);

        Long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();

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
