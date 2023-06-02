package cart.dao;

import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
