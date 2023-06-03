package shop.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import shop.persistence.entity.OrderProductEntity;
import shop.persistence.entity.detail.OrderProductDetail;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class OrderProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<OrderProductDetail> detailRowMapper =
            (rs, rowNum) -> new OrderProductDetail(
                    rs.getLong("order_product.id"),
                    rs.getLong("order_product.order_id"),
                    rs.getLong("product.id"),
                    rs.getInt("order_product.ordered_product_price"),
                    rs.getInt("order_product.quantity"),
                    rs.getString("product.name"),
                    rs.getInt("product.price"),
                    rs.getString("product.image_url")
            );

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingGeneratedKeyColumns();
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(OrderProductEntity orderProductEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(orderProductEntity);

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    public void insertAll(List<OrderProductEntity> orderProductEntities) {
        String sql = "INSERT INTO order_product " +
                "(order_id, product_id, ordered_product_price, quantity) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, orderProductEntities, orderProductEntities.size(),
                (PreparedStatement ps, OrderProductEntity entity) -> {
                    ps.setLong(1, entity.getOrderId());
                    ps.setLong(2, entity.getProductId());
                    ps.setLong(3, entity.getOrderedProductPrice());
                    ps.setInt(4, entity.getQuantity());
                });
    }

    public List<OrderProductDetail> findAllByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_product " +
                "INNER JOIN product ON order_product.product_id = product.id " +
                "WHERE order_product.order_id = ?";

        return jdbcTemplate.query(sql, detailRowMapper, orderId);
    }

    public List<OrderProductDetail> findAllByOrderIds(List<Long> orderIds) {
        String sql = "SELECT * FROM order_product " +
                "INNER JOIN product ON order_product.product_id = product.id " +
                "WHERE order_product.order_id in (:ids)";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", orderIds);

        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, detailRowMapper);
    }
}
