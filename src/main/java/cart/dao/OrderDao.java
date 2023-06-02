package cart.dao;

import cart.domain.Order;
import cart.domain.OrderItem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Order order) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into orders(member_id) values(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getOrderingMember().getId());

            return ps;
        }, keyHolder);


        final long orderId = (long) Objects.requireNonNull(keyHolder.getKeys().get("id"));
        final List<OrderItem> orderItems = order.getOrderItems();

        jdbcTemplate.batchUpdate("insert into order_items(order_id, name, price, quantity, image_url) values(?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final OrderItem orderItem = orderItems.get(i);
                final String name = orderItem.getName();
                final int price = orderItem.getPrice();
                final int quantity = orderItem.getQuantity();
                final String imageUrl = orderItem.getImageUrl();

                ps.setLong(1, orderId);
                ps.setString(2, name);
                ps.setInt(3, price);
                ps.setInt(4, quantity);
                ps.setString(5, imageUrl);
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });

        return orderId;
    }

//    public List<Order> findByMember(Member member) {
//
//    }
//
//    public Order findById(long orderId) {
//
//    }
}
