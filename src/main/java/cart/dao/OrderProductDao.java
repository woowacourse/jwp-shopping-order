package cart.dao;

import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private static final RowMapper<OrderProductEntity> ROW_MAPPER = (rs, rowNum) -> new OrderProductEntity(
            rs.getLong("order_product.id"),
            rs.getLong("order_product.order_id"),
            extractProduct(rs),
            rs.getString("order_product.product_name"),
            rs.getInt("order_product.product_price"),
            rs.getString("order_product.product_image_url"),
            rs.getInt("order_product.quantity"),
            rs.getTimestamp("order_product.created_at").toLocalDateTime(),
            rs.getTimestamp("order_product.updated_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingGeneratedKeyColumns("id")
                .usingColumns("order_id", "product_name", "product_price", "product_image_url", "quantity");
    }

    private static ProductEntity extractProduct(ResultSet rs) throws SQLException {
        Long productId = rs.getLong("product.id");
        if (productId == 0) {
            return null;
        }
        return new ProductEntity(
                rs.getLong("product.id"),
                rs.getString("product.name"),
                rs.getInt("product.price"),
                rs.getString("product.image_url"),
                rs.getTimestamp("product.created_at").toLocalDateTime(),
                rs.getTimestamp("product.updated_at").toLocalDateTime()
        );
    }

    public List<OrderProductEntity> findAllByOrderId(Long orderId) {
        String sql = "SELECT order_product.id, order_product.order_id, order_product.product_name, order_product.product_price,"
                + " order_product.product_image_url, order_product.quantity, order_product.created_at, order_product.updated_at,"
                + " product.id, product.name, product.price, product.image_url, product.created_at, product.updated_at"
                + " FROM order_product"
                + " LEFT JOIN order_product_record ON order_product.id = order_product_record.order_product_id"
                + " LEFT JOIN product ON order_product_record.product_id = product.id"
                + " WHERE order_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, orderId);
    }

    public Long save(OrderProductEntity orderProductEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderProductEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }
}
