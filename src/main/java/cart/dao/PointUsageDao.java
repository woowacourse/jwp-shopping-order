package cart.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.domain.PointUsage;

@Repository
public class PointUsageDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<PointUsage> rowMapper = (rs, rowNum) ->
        new PointUsage(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("order_id"),
            rs.getLong("point_addition_id"),
            rs.getInt("amount")
        );

    public PointUsageDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("point_usage")
            .usingGeneratedKeyColumns("id");
    }

    public List<PointUsage> findAllByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, order_id, point_addition_id, amount FROM point_usage WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long insert(PointUsage pointUsage) {
        Map<String, Object> params = Map.of(
            "id", pointUsage.getId(),
            "member_id", pointUsage.getMemberId(),
            "order_id", pointUsage.getOrderId(),
            "point_addition_id", pointUsage.getPointAdditionId(),
            "amount", pointUsage.getAmount()
        );
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public void insertAll(List<PointUsage> pointUsages) {
        String sql = "INSERT INTO point_usage (member_id, order_id, point_addition_id, amount) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PointUsage pointUsage = pointUsages.get(i);
                ps.setLong(1, pointUsage.getMemberId());
                ps.setLong(2, pointUsage.getOrderId());
                ps.setLong(3, pointUsage.getPointAdditionId());
                ps.setInt(4, pointUsage.getAmount());
            }

            @Override
            public int getBatchSize() {
                return 100;
            }
        });
    }
}
