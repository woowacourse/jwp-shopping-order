package cart.dao;

import cart.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderProductDao {
    private static final RowMapper<OrderProductEntity> ORDER_PRODUCT_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new OrderProductEntity(
            resultSet.getLong("id"),
            resultSet.getLong("order_history_id"),
            resultSet.getLong("product_id"),
            resultSet.getString("name"),
            resultSet.getInt("price"),
            resultSet.getString("image_url"),
            resultSet.getInt("quantity")
    );
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertOrderProduct;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertOrderProduct = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingGeneratedKeyColumns("id");
    }

    public OrderProductEntity insert(final OrderProductEntity entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final long id = insertOrderProduct.executeAndReturnKey(parameters).longValue();
        return new OrderProductEntity(
                id,
                entity.getOrderHistoryId(),
                entity.getProductId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl(),
                entity.getQuantity()
        );
    }

    public List<OrderProductEntity> findByOrderId(final Long orderHistoryId) {
        final String sql = "SELECT * FROM order_product WHERE order_history_id = :order_history_id:";
        final Map<String, Long> parameter = Map.of("order_history_id", orderHistoryId);
        return namedParameterJdbcTemplate.query(sql, parameter, ORDER_PRODUCT_ENTITY_ROW_MAPPER);
    }
}
