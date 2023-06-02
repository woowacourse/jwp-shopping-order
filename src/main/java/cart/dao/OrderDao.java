package cart.dao;

import cart.dao.dto.OrderDto;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderDto> ORDER_DTO_ROW_MAPPER = (rs, rn) -> new OrderDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            (Long) rs.getObject("member_coupon_id"),
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

    public Long insert(final OrderDto orderDto) {
        final MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("time_stamp", orderDto.getTimeStamp().toString())
                .addValue("member_id", orderDto.getMemberId());
        orderDto.getMemberCouponId().ifPresent(id -> source.addValue("member_coupon_id", id));
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<OrderDto> findById(final Long orderId) {
        final String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ORDER_DTO_ROW_MAPPER, orderId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<OrderDto> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, ORDER_DTO_ROW_MAPPER, memberId);
    }
}
