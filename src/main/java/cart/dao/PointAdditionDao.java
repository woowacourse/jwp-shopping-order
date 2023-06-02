package cart.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.domain.PointAddition;

@Repository
public class PointAdditionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInsert;
    private final RowMapper<PointAddition> rowMapper = (rs, rowNum) ->
        new PointAddition(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("order_id"),
            rs.getInt("amount"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("expire_at").toLocalDateTime()
        );

    public PointAdditionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("point_addition")
            .usingGeneratedKeyColumns("id");
    }

    public List<PointAddition> findAllByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, order_id, amount, created_at, expire_at FROM point_addition WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long insert(PointAddition pointAddition) {
        Map<String, Object> params = Map.of(
            "member_id", pointAddition.getMemberId(),
            "order_id", pointAddition.getOrderId(),
            "amount", pointAddition.getAmount(),
            "created_at", pointAddition.getCreatedAt(),
            "expire_at", pointAddition.getExpireAt()
        );
        return simpleInsert.executeAndReturnKey(params).longValue();
    }
}
