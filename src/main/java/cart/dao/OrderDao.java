package cart.dao;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderDto> orderDtoRowMapper = (rs, rn) -> new OrderDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getTimestamp("time_stamp").toLocalDateTime()
    );

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(OrderDto orderDto) {
        Map<String, Object> params = Map.of(
                "time_stamp", orderDto.getTimeStamp(),
                "member_id", orderDto.getMemberId()
        );

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public OrderDto findById(final Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, orderDtoRowMapper, orderId);
    }

    // findById
}
