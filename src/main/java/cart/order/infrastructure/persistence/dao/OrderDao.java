package cart.order.infrastructure.persistence.dao;

import cart.common.annotation.Dao;
import cart.order.infrastructure.persistence.entity.OrderEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Dao
public class OrderDao {

    private static final RowMapper<OrderEntity> orderRowMapper =
            new BeanPropertyRowMapper<>(OrderEntity.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(OrderEntity order) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(order);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<OrderEntity> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, orderRowMapper, id)
        );
    }

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, memberId);
    }
}
