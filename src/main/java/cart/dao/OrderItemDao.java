package cart.dao;

import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private static final RowMapper<OrderItemProductDto> ORDER_PRODUCT_ROW_MAPPER = (rs, rowNum) ->
        new OrderItemProductDto(
            rs.getLong("order_item.id"),
            rs.getLong("product.id"),
            rs.getInt("order_item.quantity"),
            rs.getString("product.name"),
            rs.getInt("product.price"),
            rs.getString("product.image_url")
        );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("order_item")
            .usingGeneratedKeyColumns("id");
    }

    public void batchInsert(List<OrderItemEntity> orderItemEntities) {
        final SqlParameterSource[] array = orderItemEntities.stream()
            .map(BeanPropertySqlParameterSource::new)
            .collect(Collectors.toList())
            .toArray(new SqlParameterSource[orderItemEntities.size()]);

        insertAction.executeBatch(array);
    }

    public List<OrderItemProductDto> findAllByOrderId(long orderId) {
        String sql = "SELECT order_item.id, product.id, order_item.quantity, product.name, "
            + "product.price, product.image_url FROM order_item "
            + "INNER JOIN product ON order_item.product_id = product.id "
            + "WHERE order_id = ?";
        return jdbcTemplate.query(sql, ORDER_PRODUCT_ROW_MAPPER, orderId);
    }

}
