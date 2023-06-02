package shop.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import shop.persistence.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

@Component
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<OrderEntity> rowMapper =
            (rs, rowNum) -> new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("total_product_price"),
                    rs.getInt("delivery_price"),
                    rs.getTimestamp("expired_at").toLocalDateTime()
            );

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(OrderEntity orderEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(orderEntity);

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    public Optional<OrderEntity> findById(Long id) {
        String sql = "SELECT * FROM order WHERE id = ?";
        OrderEntity orderEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);

        return Optional.ofNullable(orderEntity);
    }

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM order where member_id = ?";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
