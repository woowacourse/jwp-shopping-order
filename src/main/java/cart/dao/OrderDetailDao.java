package cart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cart.entity.OrderDetailEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailDao {

    private final RowMapper<OrderDetailEntity> rowMapper = (rs, rowNum) -> OrderDetailEntity.builder()
            .id(rs.getLong("id"))
            .ordersId(rs.getLong("orders_id"))
            .productId(rs.getLong("product_id"))
            .productName(rs.getString("product_name"))
            .productPrice(rs.getInt("product_price"))
            .productImageUrl(rs.getString("product_image_url"))
            .orderQuantity(rs.getInt("order_quantity"))
            .build();

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDetailDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_detail")
                .usingGeneratedKeyColumns("id");
    }

    public OrderDetailEntity insert(final OrderDetailEntity orderDetailEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("orders_id", orderDetailEntity.getOrdersId());
        params.put("product_id", orderDetailEntity.getProductId());
        params.put("product_name", orderDetailEntity.getProductName());
        params.put("product_price", orderDetailEntity.getProductPrice());
        params.put("product_image_url", orderDetailEntity.getProductImageUrl());
        params.put("order_quantity", orderDetailEntity.getOrderQuantity());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new OrderDetailEntity(id, orderDetailEntity);
    }

    public List<OrderDetailEntity> findByOrderId(final Long orderId) {
        final String sql = "SELECT id, orders_id, product_id, product_name, product_price, product_image_url, order_quantity FROM order_detail WHERE orders_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
