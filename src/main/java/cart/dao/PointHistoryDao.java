package cart.dao;

import cart.entity.PointHistoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PointHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public PointHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(PointHistoryEntity pointHistory) {
        String sql = "INSERT INTO point_history (member_id, points_used, points_saved, order_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, pointHistory.getMemberId(), pointHistory.getPointUsed(), pointHistory.getPointSaved(), pointHistory.getOrderId());
    }
}
