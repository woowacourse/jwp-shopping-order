package cart.persistence.dao;

import cart.persistence.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderProductDao {

    private final SimpleJdbcInsert jdbcInsert;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingGeneratedKeyColumns("id");
    }

    public void createProducts(final List<OrderProductEntity> orderProductEntities) {
        final SqlParameterSource[] sources = orderProductEntities.stream()
                .map(orderProductEntity -> new MapSqlParameterSource()
                        .addValue("order_id", orderProductEntity.getOrderId())
                        .addValue("product_id", orderProductEntity.getProductId())
                        .addValue("purchased_price", orderProductEntity.getPurchasedPrice())
                        .addValue("quantity", orderProductEntity.getQuantity())
                ).toArray(SqlParameterSource[]::new);
        jdbcInsert.executeBatch(sources);
    }
}
