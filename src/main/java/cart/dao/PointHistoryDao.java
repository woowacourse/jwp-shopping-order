package cart.dao;

import cart.domain.Point;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PointHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public PointHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PointHistoryEntity> findByPointIds(List<Long> pointIds) {
        String inSql = pointIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = String.format("select * from point_history where point_id in (%s)", inSql);

        return jdbcTemplate.query(sql, new PointHistoryRowMapper());
    }

    public void save(Long orderId, Long pointId, int usedPoint) {
        String sql = "insert into point_history(orders_id, point_id, used_point) values(?, ?, ?)";

        jdbcTemplate.update(sql, orderId, pointId, usedPoint);
    }


    public void saveAll(Long orderId, List<PointEntity> points) {
        String sql = "insert into point_history(orders_id, point_id, used_point) values(?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, orderId);
                        ps.setLong(2, points.get(i).getId());
                        ps.setInt(3, points.get(i).getValue());
                    }

                    @Override
                    public int getBatchSize() {
                        return points.size();
                    }
                });
    }

    public void deleteByOrderId(Long orderId) {
        String sql = "delete from point_history where orders_id = ?";

        jdbcTemplate.update(sql, orderId);
    }

    private static class PointHistoryRowMapper implements RowMapper<PointHistoryEntity> {

        @Override
        public PointHistoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            long orderId = rs.getLong("orders_id");
            long pointId = rs.getLong("point_id");
            int usedPoint = rs.getInt("used_point");

            return new PointHistoryEntity(id, orderId, pointId, usedPoint);
        }
    }
}
