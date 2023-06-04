package cart.dao;

import cart.entity.OrderProductEntity;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private static final RowMapper<OrderProductEntity> ROW_MAPPER = ((rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long orderId = rs.getLong("orders_id");
        Long productId = rs.getLong("product_id");
        int quantity = rs.getInt("quantity");
        String name = rs.getString("product_name");
        BigDecimal price = rs.getBigDecimal("product_price");
        String imageUrl = rs.getString("product_image_url");
        return new OrderProductEntity(id, orderId, productId, quantity, name, price, imageUrl);
    });

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(List<OrderProductEntity> orderProducts) {
        String sql = "INSERT INTO orders_product "
                + "(orders_id, product_id, quantity, product_name, product_price, product_image_url) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, orderProducts, orderProducts.size(), (((ps, orderProduct) -> {
            ps.setLong(1, orderProduct.getOrderId());
            ps.setLong(2, orderProduct.getProductId());
            ps.setInt(3, orderProduct.getQuantity());
            ps.setString(4, orderProduct.getProductName());
            ps.setBigDecimal(5, orderProduct.getProductPrice());
            ps.setString(6, orderProduct.getProductImageUrl());
        })));
    }

    public List<OrderProductEntity> findAll() {
        String sql = "select * from orders_product";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public List<OrderProductEntity> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM orders_product WHERE orders_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, orderId);
    }
}
