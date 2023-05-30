package cart.dao;

import cart.dao.entity.OrderEntity;
import cart.domain.order.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("memberId"),
                    rs.getLong("shippingFee"),
                    rs.getLong("totalPrice"),
                    rs.getTimestamp("created_at").toString()
            );

    public OrderDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrder(final Order order) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(order);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderEntity> findByMemberId(Long memberId) {
        String sql = "select * from orders where member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
