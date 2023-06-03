package cart.db.dao;

import cart.db.entity.OrderProductDetailEntity;
import cart.db.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void createAll(final List<OrderProductEntity> orderProductEntities) {
        String sql = "INSERT INTO order_product (order_id, product_id, ordered_product_price, quantity) VALUES (:orderId, :productId, :orderedProductPrice, :quantity)";
        SqlParameterSource[] batchParams = orderProductEntities.stream()
                .map(entity -> new MapSqlParameterSource()
                        .addValue("orderId", entity.getOrderId())
                        .addValue("productId", entity.getProductId())
                        .addValue("orderedProductPrice", entity.getOrderedProductPrice())
                        .addValue("quantity", entity.getQuantity()))
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }


    public List<OrderProductDetailEntity> findByOrderId(final Long orderId) {
        String sql = "SELECT order_product.id, order_id, quantity, " +
                "product_id, product.name, ordered_product_price, product.image_url " +
                "FROM order_product JOIN product ON order_product.product_id = product.id " +
                "WHERE order_id = ?";
        return jdbcTemplate.query(sql, new OrderProductDetailEntityRowMapper(), orderId);

    }

    public List<OrderProductDetailEntity> findByOrderIds(final List<Long> orderIds) {
        String sql = "SELECT order_product.id, order_id, quantity, " +
                "product_id, product.name, ordered_product_price, product.image_url " +
                "FROM order_product JOIN product ON order_product.product_id = product.id " +
                "WHERE order_product.order_id IN (:ids)";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", orderIds);
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new OrderProductDetailEntityRowMapper());
    }

    private static class OrderProductDetailEntityRowMapper implements RowMapper<OrderProductDetailEntity> {
        @Override
        public OrderProductDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderProductDetailEntity(
                    rs.getLong("order_product.id"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id"),
                    rs.getString("product.name"),
                    rs.getInt("ordered_product_price"),
                    rs.getString("product.image_url"),
                    rs.getInt("quantity")
            );
        }
    }
}
