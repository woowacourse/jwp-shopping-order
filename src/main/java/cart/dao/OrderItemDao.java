package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderItem> findAll() {
        String sql = "select * from orders_item left join product on orders_item.product_id = product.id";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    private static class OrderRowMapper implements RowMapper<OrderItem> {

        @Override
        public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            long productId = rs.getLong("product_id");
            String name = rs.getString("product.name");
            int price = rs.getInt("product.price");
            String imageUrl = rs.getString("product.image_url");

            Product product = new Product(productId, name, price, imageUrl);
            int quantity = rs.getInt("quantity");
            int totalPrice = rs.getInt("total_price");
            return new OrderItem(product, quantity, totalPrice);
        }
    }
}
