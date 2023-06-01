package cart.dao;

import cart.entity.OrderDetailEntity;
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
public class OrderDetailDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDetailDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("order_detail");
    }

    public List<OrderDetailEntity> getAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM order_detail " +
                "INNER JOIN orders ON order_detail.order_id = orders.id " +
                "WHERE orders.member_id = ?";
        return jdbcTemplate.query(sql, new OrderDetailRowMapper(), memberId);
    }

    public List<OrderDetailEntity> getAllByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_detail " +
                "INNER JOIN orders ON order_detail.orders_id = orders.id " +
                "WHERE orders.id = ?";
        return jdbcTemplate.query(sql, new OrderDetailRowMapper(), orderId);
    }

    public Long insert(OrderDetailEntity entity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    private static class OrderDetailRowMapper implements RowMapper<OrderDetailEntity> {
        @Override
        public OrderDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderDetailEntity(
                    rs.getLong("id"),
                    rs.getLong("orders_id"),
                    rs.getString("product_name"),
                    rs.getString("product_image"),
                    rs.getInt("product_quantity"),
                    rs.getInt("product_price")
            );
        }
    }
}
