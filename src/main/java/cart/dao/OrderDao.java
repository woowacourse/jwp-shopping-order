package cart.dao;

import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("`order`")
            .usingColumns("member_id", "discounted_amount", "address", "delivery_amount")
            .usingGeneratedKeyColumns("id");
    }

    public Order save(final Order order, final Long memberId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberId);
        params.put("discounted_amount", order.discountProductAmount().getValue());
        params.put("address", order.getAddress());
        params.put("delivery_amount", order.getDeliveryAmount().getValue());
        final long orderId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        final String productOrderSql = "INSERT INTO product_order(product_id, order_id) "
            + "VALUES (?, ?)";
        final Products products = order.getProducts();
        for (final Product product : products.getValue()) {
            jdbcTemplate.update(productOrderSql, product.getId(), orderId);
        }
        return new Order(orderId, order.getProducts(), order.getCoupon(), order.getDeliveryAmount(),
            order.getAddress());
    }
}
