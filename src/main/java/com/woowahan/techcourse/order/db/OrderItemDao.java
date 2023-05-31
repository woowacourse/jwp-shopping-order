package com.woowahan.techcourse.order.db;

import com.woowahan.techcourse.order.domain.OrderItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(long orderId, List<OrderItem> couponIds) {
        for (OrderItem orderItem : couponIds) {
            insert(orderId, orderItem);
        }
    }

    private void insert(long orderId, OrderItem orderItem) {
        simpleJdbcInsert.execute(orderItemToMap(orderId, orderItem));
    }

    private Map<String, Object> orderItemToMap(long orderId, OrderItem orderItem) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("order_id", orderId);
        parameters.put("cart_item_id", orderItem.getCartItemId());
        parameters.put("product_id", orderItem.getProductId());
        parameters.put("quantity", orderItem.getQuantity());
        parameters.put("product_price", orderItem.getPrice());
        parameters.put("product_name", orderItem.getName());
        parameters.put("product_image", orderItem.getImageUrl());
        return parameters;
    }

    public List<OrderItem> findAllByOrderId(long orderId) {
        return jdbcTemplate.query("SELECT * FROM order_item WHERE order_id = ?",
                (rs, rowNum) -> new OrderItem(
                        rs.getLong("cart_item_id"),
                        rs.getInt("quantity"),
                        rs.getLong("product_id"),
                        rs.getInt("product_price"),
                        rs.getString("product_name"),
                        rs.getString("product_image")
                ),
                orderId);
    }
}
