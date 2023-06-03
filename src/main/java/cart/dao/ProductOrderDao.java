package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOrderDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductOrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer count(final Long productId, final Long orderId) {
        final String sql =
            "SELECT count(*) "
                + "FROM product_order "
                + "WHERE product_id = ? "
                + "  AND order_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, productId, orderId);
    }
}
