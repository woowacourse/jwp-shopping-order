package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersCartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    public OrdersCartItemDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_cart_item")
                .usingGeneratedKeyColumns("id");
    }
    public void createOrdersIdCartItemId(final long ordersId, final long productId,final int quantity){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orders_id",ordersId)
                .addValue("product_id",productId)
                .addValue("quantity",quantity);
        simpleJdbcInsert.execute(parameterSource);
    }
}
