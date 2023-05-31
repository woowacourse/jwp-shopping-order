package cart.dao;

import cart.domain.OrderInfo;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderInfoDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertOrderInfos;

    public OrderInfoDao(JdbcTemplate jdbcTemplate) {
        this.insertOrderInfos = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_info")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
    }

    public OrderInfo getOrderInfoById(Long id) {
        String sql = "SELECT * FROM order_info WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderInfoRowMapper(), id);
    }

    public List<OrderInfo> getOrderInfoByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_info WHERE order_id = ?";
        return jdbcTemplate.query(sql, new OrderInfoRowMapper(), orderId);
    }

    public Long save(OrderInfo orderInfo) {
        Map<String, Object> parameters = new HashMap<>();
        Product product = orderInfo.getProduct();
        parameters.put("order_id", orderInfo.getOrderId());
        parameters.put("product_id", product.getId());
        parameters.put("name", product.getName());
        parameters.put("price", product.getPrice());
        parameters.put("image_url", product.getImageUrl());
        parameters.put("quantity", orderInfo.getQuantity());

        return insertOrderInfos.executeAndReturnKey(parameters).longValue();
    }

    private static class OrderInfoRowMapper implements RowMapper<OrderInfo> {
        @Override
        public OrderInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long productId = rs.getLong("product_id");
            String name = rs.getString("name");
            Long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            return new OrderInfo(
                    rs.getLong("id"),
                    rs.getLong("order_id"),
                    new Product(productId, name, price.intValue(), imageUrl),
                    rs.getLong("quantity")
            );
        }
    }
}
