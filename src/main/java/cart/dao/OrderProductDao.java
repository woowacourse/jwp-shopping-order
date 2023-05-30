package cart.dao;

import cart.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {
    private final SimpleJdbcInsert insertOrderProduct;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.insertOrderProduct = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_history")
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
}
