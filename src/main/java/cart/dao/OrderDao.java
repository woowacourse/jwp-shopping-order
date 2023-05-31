package cart.dao;

import cart.dao.dto.OrderDto;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public Optional<OrderDto> findById(final Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, orderDtoRowMapper, orderId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

}
