package com.woowahan.techcourse.order.db;

import com.woowahan.techcourse.order.domain.OrderItem;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class OrderItemDao {

    private static final RowMapper<OrderItem> ROW_MAPPER = (rs, rowNum) -> new OrderItem(
            rs.getLong("id"),
            rs.getInt("quantity"),
            rs.getLong("product_id"),
            rs.getInt("product_price"),
            rs.getString("product_name"),
            rs.getString("product_image")
    );

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
        MapSqlParameterSource parameters = orderItemToParameters(orderId, orderItem);
        simpleJdbcInsert.execute(parameters);
    }

    private MapSqlParameterSource orderItemToParameters(long orderId, OrderItem orderItem) {
        return new MapSqlParameterSource()
                .addValue("order_id", orderId)
                .addValue("product_id", orderItem.getProductId())
                .addValue("quantity", orderItem.getQuantity())
                .addValue("product_price", orderItem.getPrice())
                .addValue("product_name", orderItem.getName())
                .addValue("product_image", orderItem.getImageUrl());
    }

    public List<OrderItem> findAllByOrderId(long orderId) {
        return jdbcTemplate.query("SELECT * FROM order_item WHERE order_id = ?", ROW_MAPPER, orderId);
    }
}
