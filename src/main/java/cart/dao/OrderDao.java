package cart.dao;

import cart.dao.dto.order.OrderWithMemberDto;
import cart.dao.dto.order.OrderDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderWithMemberDto> ROW_MAPPER = (rs, rowNum) ->
        new OrderWithMemberDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getTimestamp("created_at"),
            rs.getString("email"),
            rs.getString("password")
        );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("`order`")
            .usingGeneratedKeyColumns("id")
            .usingColumns("member_id");
    }

    public long save(OrderDto orderDto) {
        Number generatedKey = insertAction.executeAndReturnKey(
            Map.of("member_id", orderDto.getMemberId())
        );

        return Objects.requireNonNull(generatedKey).longValue();
    }

    public Optional<OrderWithMemberDto> findById(long id) {
        String sql = "SELECT `order`.id, member_id, created_at, email, password  FROM `order` "
            + "INNER JOIN member ON `order`.member_id = member.id WHERE `order`.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OrderWithMemberDto> findAllByMemberId(long memberId) {
        String sql = "SELECT `order`.id, member_id, created_at, email, password FROM `order` "
            + "INNER JOIN member ON `order`.member_id = member.id WHERE member_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }
}
