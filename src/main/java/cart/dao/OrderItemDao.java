package cart.dao;

import cart.domain.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingColumns("order_id", "product_id", "product_name",
                        "product_image_url", "product_price", "quantity")
                .usingGeneratedKeyColumns("id");
    }

    public long save(OrderItem orderItem) {
        final SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderItem);
        return this.simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }
}
