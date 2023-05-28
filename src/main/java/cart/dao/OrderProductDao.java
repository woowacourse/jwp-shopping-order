package cart.dao;

import cart.domain.OrderProducts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private final SimpleJdbcInsert jdbcInsert;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingGeneratedKeyColumns("id");
    }

    public void createProducts(final Long orderId, final OrderProducts products) {
        final SqlParameterSource[] sources = products.getProducts().stream()
                .map(product -> new MapSqlParameterSource()
                        .addValue("order_id", orderId)
                        .addValue("product_id", product.getProduct().getId())
                        .addValue("quantity", product.getQuantity())
                ).toArray(SqlParameterSource[]::new);
        jdbcInsert.executeBatch(sources);
    }
}
