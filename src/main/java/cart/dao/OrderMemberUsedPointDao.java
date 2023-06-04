package cart.dao;

import cart.domain.point.UsedPoint;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderMemberUsedPointDao {

    private static final RowMapper<UsedPoint> rowMapper = (rs, rowNum) ->
            new UsedPoint(
                    rs.getLong("id"),
                    rs.getLong("used_reward_point_id"),
                    rs.getInt("used_point")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderMemberUsedPointDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
                                   NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("order_member_used_point")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long save(UsedPoint usedPoint, Long orderId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("order_id", orderId)
                .addValue("used_reward_point_id", usedPoint.getPointId())
                .addValue("used_point", usedPoint.getUsedPoint());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public void saveAll(List<UsedPoint> usedPoints, Long orderId) {
        String sql = "INSERT INTO order_member_used_point (order_id, used_reward_point_id, used_point) "
                + "VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        UsedPoint usedPoint = usedPoints.get(i);
                        ps.setLong(1, orderId);
                        ps.setLong(2, usedPoint.getPointId());
                        ps.setInt(3, usedPoint.getUsedPoint());
                    }

                    @Override
                    public int getBatchSize() {
                        return usedPoints.size();
                    }
                }
        );
    }

    public List<UsedPoint> getAllUsedPointByOrderId(Long orderId) {
        String sql = "SELECT id, used_reward_point_id, used_point FROM order_member_used_point WHERE order_id = :order_id";
        SqlParameterSource source = new MapSqlParameterSource("order_id", orderId);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }

    public boolean isAlreadyUsedReward(Long rewardPointId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM order_member_used_point WHERE used_reward_point_id = :가used_reward_point_id)";
        SqlParameterSource source = new MapSqlParameterSource("used_reward_point_id", rewardPointId);
        return namedParameterJdbcTemplate.queryForObject(sql, source, Boolean.class);
    }

    public void deleteAll(List<UsedPoint> usedPoints) {
        String sql = "DELETE FROM order_member_used_point WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        UsedPoint usedPoint = usedPoints.get(i);
                        ps.setLong(1, usedPoint.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return usedPoints.size();
                    }
                }
        );
    }
}
