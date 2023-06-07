package cart.dao;

import cart.dao.entity.OrdersCartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersCartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrdersCartItemEntity> cartItemEntityRowMapper = (rs, rowNum) -> new OrdersCartItemEntity(
            rs.getLong("id"),
            rs.getLong("orders_id"),
            rs.getLong("product_id"),
            rs.getInt("price"),
            rs.getInt("quantity")
    );

    public OrdersCartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createOrdersIdCartItemId(final long ordersId, final long productId, final int price, final int quantity) {
        final String sql = "INSERT INTO orders_cart_item(orders_id,product_id,price,quantity) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, ordersId, productId, price, quantity);
    }

    public List<OrdersCartItemEntity> findAllByOrdersId(final long ordersId) {
        final String sql = "SELECT * FROM orders_cart_item WHERE orders_id =?";
        return jdbcTemplate.query(sql, cartItemEntityRowMapper, ordersId);
    }
}
