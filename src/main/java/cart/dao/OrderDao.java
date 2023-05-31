package cart.dao;

import cart.dao.entity.OrderEntity;
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

    private static final RowMapper<OrderEntity> ROW_MAPPER = (rs, rowNum) ->
        new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getTimestamp("created_at")
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

    public long save(OrderEntity orderEntity) {
        Number generatedKey = insertAction.executeAndReturnKey(
            Map.of("member_id", orderEntity.getMemberId())
        );

        return Objects.requireNonNull(generatedKey).longValue();
    }

    public Optional<OrderEntity> findById(long id) {
        String sql = "SELECT * from `order` WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
