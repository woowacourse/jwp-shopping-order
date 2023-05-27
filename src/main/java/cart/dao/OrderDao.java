package cart.dao;

import cart.domain.CartItem;
import cart.domain.Order;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
public final class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order save(final Order order) {
        final Order persisted = savePrice(order);

        final List<CartItem> cartItems = persisted.getCartItems();
        final String sql = "INSERT INTO ordered_product(order_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, persisted.getId());
                ps.setLong(2, cartItems.get(i).getProduct().getId());
                ps.setInt(3, cartItems.get(i).getQuantity());
            }

            @Override
            public int getBatchSize() {
                return cartItems.size();
            }
        });

        return persisted;
    }

    private Order savePrice(final Order order) {
        String sql = "INSERT INTO `order`(price, member_id) VALUES (?, ?)";

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getPrice());
            ps.setLong(2, order.getMember().getId());

            return ps;
        }, keyHolder);

        final Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Order(key, order.getMember(), order.getPrice(), order.getCartItems());
    }
}
