package cart.dao;

import cart.domain.OrderProduct;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(OrderProduct orderProduct) {
        String sql = "INSERT INTO order_product (order_history_id, product_id, name, price, image_url, quantity) " +
                "VALUES (:orderHistoryId, :productId, :name, :price, :imageUrl, :quantity)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("orderHistoryId", orderProduct.getOrderHistory().getId());
        params.addValue("productId", orderProduct.getProductId());
        params.addValue("name", orderProduct.getName());
        params.addValue("price", orderProduct.getPrice());
        params.addValue("imageUrl", orderProduct.getImageUrl());
        params.addValue("quantity", orderProduct.getQuantity());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void insertAll(List<OrderProduct> orderProducts) {
        String sql = "INSERT INTO order_product (order_history_id, product_id, name, price, image_url, quantity) " +
                "VALUES (:orderHistoryId, :productId, :name, :price, :imageUrl, :quantity)";

        MapSqlParameterSource[] paramsBatch = new MapSqlParameterSource[orderProducts.size()];

        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProduct orderProduct = orderProducts.get(i);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("orderHistoryId", orderProduct.getOrderHistory().getId());
            params.addValue("productId", orderProduct.getProductId());
            params.addValue("name", orderProduct.getName());
            params.addValue("price", orderProduct.getPrice());
            params.addValue("imageUrl", orderProduct.getImageUrl());
            params.addValue("quantity", orderProduct.getQuantity());

            paramsBatch[i] = params;
        }

        jdbcTemplate.batchUpdate(sql, paramsBatch);
    }
}
