package cart.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.domain.OrderItem;
import cart.domain.Product;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(Long orderId, List<OrderItem> orders) {
        SqlParameterSource[] parameterSources = orders.stream()
                .map(orderItem -> new MapSqlParameterSource()
                        .addValue("order_id", orderId)
                        .addValue("product_id", orderItem.getOrderedProduct().getId())
                        .addValue("name", orderItem.getOrderedProduct().getName())
                        .addValue("price", orderItem.getOrderedProduct().getPrice().getValue())
                        .addValue("image_url", orderItem.getOrderedProduct().getImageUrl())
                        .addValue("quantity", orderItem.getQuantity())
                )
                .toArray(SqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(parameterSources);
    }

    public List<OrderItem> selectAllOf(Long orderId) {
        String sql = "select * from order_item where order_id = ?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new OrderItem(
                        rs.getLong("id"),
                        new Product(
                                rs.getLong("product_id"),
                                rs.getString("name"),
                                rs.getInt("price"),
                                rs.getString("image_url")
                        ),
                        rs.getInt("quantity")
                ),
                orderId
        );
    }
}
