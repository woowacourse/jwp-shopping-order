package cart.dao;

import cart.domain.OrderItem;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(OrderItem orderItem) {
        String sql = "INSERT INTO order_item (order_history_id, product_id, name, price, image_url, quantity) " +
                "VALUES (:orderHistoryId, :productId, :name, :price, :imageUrl, :quantity)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orderHistoryId", orderItem.getOrderHistory().getId());
        params.addValue("productId", orderItem.getProductId());
        params.addValue("name", orderItem.getName());
        params.addValue("price", orderItem.getPrice());
        params.addValue("imageUrl", orderItem.getImageUrl());
        params.addValue("quantity", orderItem.getQuantity());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void insertAll(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item (order_history_id, product_id, name, price, image_url, quantity) " +
                "VALUES (:orderHistoryId, :productId, :name, :price, :imageUrl, :quantity)";

        MapSqlParameterSource[] paramsBatch = new MapSqlParameterSource[orderItems.size()];

        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("orderHistoryId", orderItem.getOrderHistory().getId());
            params.addValue("productId", orderItem.getProductId());
            params.addValue("name", orderItem.getName());
            params.addValue("price", orderItem.getPrice());
            params.addValue("imageUrl", orderItem.getImageUrl());
            params.addValue("quantity", orderItem.getQuantity());

            paramsBatch[i] = params;
        }

        jdbcTemplate.batchUpdate(sql, paramsBatch);
    }
}
