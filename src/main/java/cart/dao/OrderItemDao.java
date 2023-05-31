package cart.dao;

import cart.domain.Product;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    private static RowMapper<OrderItemEntity> rowMapper() {
        return (rs, rowNum) -> new OrderItemEntity(
                rs.getLong("id"),
                rs.getLong("order_id"),
                new ProductEntity(
                        rs.getLong("product_id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                ),
                rs.getInt("quantity")
        );
    }

    public Long create(Long orderId, Product product, int quantity) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("order_id", orderId)
                .addValue("product_id", product.getId())
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl())
                .addValue("quantity", quantity);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderItemEntity> findAllByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";

        return jdbcTemplate.query(sql, rowMapper(), orderId);
    }
}
