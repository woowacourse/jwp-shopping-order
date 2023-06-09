package cart.dao;

import cart.domain.OrderItem;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderItem> rowMapper = (resultSet, rowNumber) -> new OrderItem(
            resultSet.getLong("id"),
            resultSet.getLong("order_id"),
            resultSet.getLong("product_id"),
            resultSet.getString("product_name"),
            resultSet.getString("product_image_url"),
            resultSet.getInt("product_price"),
            resultSet.getInt("quantity")
    );

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingColumns("order_id", "product_id", "product_name",
                        "product_image_url", "product_price", "quantity")
                .usingGeneratedKeyColumns("id");
    }

    public long save(OrderItem orderItem) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderItem);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT id, order_id, product_id, product_name, product_image_url, product_price, quantity "
                + "FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
