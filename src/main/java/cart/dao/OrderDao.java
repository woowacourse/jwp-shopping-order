package cart.dao;

import cart.domain.CartItem;
import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order (member_id) VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            return ps;
        }, keyHolder);

        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        String relatedSql = "INSERT INTO order_items (order_id, cart_id) VALUES(?, ?)";

        for (CartItem cartItem : order.getCartItems()) {
            jdbcTemplate.update(relatedSql, orderId, cartItem.getId());
        }

        return orderId;
    }


}
