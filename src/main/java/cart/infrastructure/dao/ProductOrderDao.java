package cart.infrastructure.dao;

import cart.entity.ProductOrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOrderDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductOrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(final ProductOrderEntity productOrder) {
        final String sql = "INSERT INTO product_order(product_id, order_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, productOrder.getProductId(), productOrder.getOrderId(), productOrder.getQuantity());
    }
}
