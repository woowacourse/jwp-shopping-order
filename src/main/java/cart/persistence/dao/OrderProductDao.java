package cart.persistence.dao;

import cart.persistence.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_product")
                .usingColumns("name", "price", "image_url", "quantity", "order_id", "product_id")
                .usingGeneratedKeyColumns("id");
    }

    public Long add(OrderProductEntity orderProductEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderProductEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public List<OrderProductEntity> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_product WHERE order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, new OrderProductEntityRowMapper());
    }

    private static class OrderProductEntityRowMapper implements RowMapper<OrderProductEntity> {
        @Override
        public OrderProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getInt("quantity"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id")
            );
        }
    }
}
