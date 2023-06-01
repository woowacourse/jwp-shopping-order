package cart.dao;

import cart.domain.CartItem;
import cart.domain.Order;
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
        final List<CartItem> cartItems = order.getCartItems();

        jdbcTemplate.batchUpdate("insert into order_items(order_id, product_id, quantity) values(?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final CartItem cartItem = cartItems.get(i);
                final Long productId = cartItem.getProduct().getId();
                final int quantity = cartItem.getQuantity();

                ps.setLong(1, orderId);
                ps.setLong(2, productId);
                ps.setInt(3, quantity);
            }

            @Override
            public int getBatchSize() {
                return cartItems.size();
            }
        });

        return orderId;
    }
}
