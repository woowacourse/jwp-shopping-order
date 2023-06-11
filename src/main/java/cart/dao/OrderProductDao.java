package cart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.repository.OrderProductEntity;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInsert;
    private final RowMapper<OrderProductEntity> rowMapper = (rs, rowNum) ->
        new OrderProductEntity(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
        );

    public OrderProductDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("order_product")
            .usingGeneratedKeyColumns("id");
    }

    public List<Long> insertAll(List<OrderProductEntity> orderProducts) {
        List<Long> orderProductIds = new ArrayList<>();
        for (OrderProductEntity orderProduct : orderProducts) {
            orderProductIds.add(insert(orderProduct));
        }
        return orderProductIds;
    }

    public Long insert(OrderProductEntity orderProduct) {
        Map<String, Object> params = Map.of(
            "order_id", orderProduct.getOrderId(),
            "product_id", orderProduct.getProductId(),
            "quantity", orderProduct.getQuantity()
        );
        return simpleInsert.executeAndReturnKey(params).longValue();
    }
}
