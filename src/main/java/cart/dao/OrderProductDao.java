package cart.dao;

import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
                .usingColumns("order_id", "product_id", "product_name", "product_price", "product_image_url", "quantity");
    }

    private static ProductEntity extractProduct(ResultSet rs) throws SQLException {
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
                + " LEFT JOIN product ON order_product.product_id = product.id"
                + " WHERE order_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, orderId);
    }

    public int saveAll(List<OrderProductEntity> orderProductEntities) {
        String sql = "INSERT INTO order_product(order_id, product_id, product_name, product_price, product_image_url, quantity) VALUES(?, ?, ?, ?, ?, ?)";
        int[] rowsAffected = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, orderProductEntities.get(i).getOrderId());
                ps.setLong(2, orderProductEntities.get(i).getProductId());
                ps.setString(3, orderProductEntities.get(i).getProductName());
                ps.setInt(4, orderProductEntities.get(i).getProductPrice());
                ps.setString(5, orderProductEntities.get(i).getProductImageUrl());
                ps.setInt(6, orderProductEntities.get(i).getQuantity());
            }

            @Override
            public int getBatchSize() {
                return orderProductEntities.size();
            }
        });
        return Arrays.stream(rowsAffected).sum();
    }

    public Long save(OrderProductEntity orderProductEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderProductEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }
}
