package cart.dao;

import java.util.HashMap;
import java.util.Map;

import cart.entity.OrderDetailEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDetailDao(final JdbcTemplate jdbcTemplate) {
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
}
