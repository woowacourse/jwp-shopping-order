package cart.dao;

import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderProductEntity> orderProductEntityRowMapper = (rs, rowNum) -> {
        ProductEntity productEntity = new ProductEntity(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getInt("product_price"),
                rs.getString("product_image_url")
        );

        return new OrderProductEntity(
                rs.getLong("id"),
                rs.getLong("order_id"),
                productEntity,
                rs.getInt("quantity"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    };


    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("order_product")
                .usingColumns("order_id",
                        "product_id",
                        "product_name",
                        "product_price",
                        "product_image_url",
                        "quantity"
                );
    }

    public Long save(OrderProductEntity orderProductEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderProductEntity)).longValue();
    }
}
