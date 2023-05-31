package cart.application;

import cart.domain.OrderedItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
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
                    "INSERT INTO ordered_item (orders_id, product_name, product_price, product_image, product_quantity, product_is_discounted, product_discount_rate) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderId);
            ps.setString(2, orderedItem.getProductName());
            ps.setInt(3, orderedItem.getProductPrice());
            ps.setString(4, orderedItem.getProductImage());
            ps.setInt(5, orderedItem.getProductQuantity());
            ps.setInt(6, orderedItem.getIsDiscounted() ? 1 : 0);
            ps.setInt(7, orderedItem.getDiscountedRate());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();

    }

    public List<OrderedItem> findByOrderId(Long id) {
        String sql = "SELECT * FROM ordered_item where orders_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long ordersId = rs.getLong("orders_id");
            String productName = rs.getString("product_name");
            int productPrice = rs.getInt("product_price");
            String productImage = rs.getString("product_image");
            int productQuantity = rs.getInt("product_quantity");
            boolean isDiscounted = (rs.getInt("product_is_discounted") == 1) ? true : false;
            int discountRate = rs.getInt("product_discount_rate");

            return new OrderedItem(id, ordersId, productName, productPrice, productImage, productQuantity, isDiscounted, discountRate);
        });
    }
}
