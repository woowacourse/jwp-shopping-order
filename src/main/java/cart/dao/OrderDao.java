package cart.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.dao.dto.OrderDto;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(OrderDto orderDto) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderDto);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public OrderDto selectBy(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new OrderDto(
                        rs.getLong("id"),
                        rs.getLong("member_id"),
                        rs.getObject("created_at", LocalDateTime.class)
                ),
                id
        );
    }

    public List<OrderDto> selectAllBy(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new OrderDto(
                        rs.getLong("id"),
                        rs.getLong("member_id"),
                        rs.getObject("created_at", LocalDateTime.class)
                ),
                memberId
        );
    }
}
