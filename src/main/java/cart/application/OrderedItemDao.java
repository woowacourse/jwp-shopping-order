package cart.application;

import cart.domain.OrderedItem;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class OrderedItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderedItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createOrderedItems(Long orderId, OrderedItem orderedItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ordered_item (orders_id, product_name, product_price, product_image, product_quantity) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderId);
            ps.setString(2, orderedItem.getProductName());
            ps.setInt(3, orderedItem.getProductPrice());
            ps.setString(4, orderedItem.getProductImage());
            ps.setInt(5, orderedItem.getProductQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();

    }
}
