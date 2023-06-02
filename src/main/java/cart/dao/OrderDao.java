package cart.dao;

import cart.dao.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final OrderEntity orderEntity) {
        final BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(orderEntity);
        return this.simpleJdbcInsert.executeAndReturnKey(beanPropertySqlParameterSource).longValue();
    }

    public List<OrderEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM order WHERE member_id = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new OrderEntity(rs.getLong("id"), rs.getLong("member_id"), rs.getTimestamp("order_time"));
        }, memberId);
    }

    public OrderEntity findById(final Long orderId) {
        final String sql = "SELECT * FROM order WHERE id = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new OrderEntity(rs.getLong("id"), rs.getLong("member_id"), rs.getTimestamp("order_time"));
        }, orderId);
    }
}
