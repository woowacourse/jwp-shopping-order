package cart.dao;

import cart.dao.entity.CartItemEntity;
import cart.dao.entity.OrdersCartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersCartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrdersCartItemEntity> cartItemEntityRowMapper = (rs, rowNum) -> new OrdersCartItemEntity(
      rs.getLong("id"),
      rs.getLong("orders_id"),
      rs.getLong("product_id"),
            rs.getInt("quantity")
    );
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

    public List<OrdersCartItemEntity> findAllByOrdersId(final long ordersId){
        final String sql = "SELECT * FROM orders_cart_item WHERE orders_id =?";
        return jdbcTemplate.query(sql,cartItemEntityRowMapper,ordersId );
    }
}
