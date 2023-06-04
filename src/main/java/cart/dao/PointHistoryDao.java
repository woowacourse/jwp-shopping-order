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

    private static final String DUMMY = "-1";

    private final JdbcTemplate jdbcTemplate;

    public PointHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PointHistoryEntity> findByPointIds(List<Long> pointIds) {
        String inSql = getInSql(pointIds);
        String sql = String.format("select id, orders_id, point_id, used_point from point_history where point_id in (%s)", inSql);

        return jdbcTemplate.query(sql, new PointHistoryRowMapper());
    }

    private String getInSql(List<Long> pointIds) {
        String inSql = pointIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        if (inSql.isEmpty()) {
            inSql = DUMMY;
        }
        return inSql;
    }

    public List<PointHistoryEntity> findByOrderId(Long orderId) {
        String sql = "select id, orders_id, point_id, used_point from point_history where orders_id = ?";

        return jdbcTemplate.query(sql, new PointHistoryRowMapper(), orderId);
    }

    public boolean isIn(Long pointId) {
        String sql = "select exists (select id from point_history where point_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, pointId);
    }

    public void save(Long orderId, Long pointId, int usedPoint) {
        String sql = "insert into point_history(orders_id, point_id, used_point) values(?, ?, ?)";

        jdbcTemplate.update(sql, orderId, pointId, usedPoint);
    }


    public void saveAll(Long orderId, List<PointEntity> pointEntities) {
        String sql = "insert into point_history(orders_id, point_id, used_point) values(?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, orderId);
                        ps.setLong(2, pointEntities.get(i).getId());
                        ps.setInt(3, pointEntities.get(i).getValue());
                    }

                    @Override
                    public int getBatchSize() {
                        return pointEntities.size();
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
