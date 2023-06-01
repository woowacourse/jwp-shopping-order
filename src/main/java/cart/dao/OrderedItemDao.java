package cart.dao;

import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderedItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderedItemDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("ordered_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Long productId, Long orderId, Integer quantity) {
        Map<String, Object> params = new HashMap<>();
        params.put("product_id", productId);
        params.put("order_id", orderId);
        params.put("quantity", quantity);

        return insertAction.executeAndReturnKey(params).longValue();

    }
}
