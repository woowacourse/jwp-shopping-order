package cart.infrastructure.dao;

import cart.entity.ProductOrderEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOrderDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ProductOrderEntity> rowMapper = (rs, rowNum) ->
            ProductOrderEntity.of(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("order_id"),
                    rs.getInt("quantity")
            );

    public ProductOrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(final ProductOrderEntity productOrder) {
        final String sql = "INSERT INTO product_order(product_id, order_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, productOrder.getProductId(), productOrder.getOrderId(), productOrder.getQuantity());
    }

    public List<ProductOrderEntity> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM product_order WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
