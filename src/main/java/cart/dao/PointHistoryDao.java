package cart.dao;

import cart.entity.PointHistoryEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PointHistoryEntity> rowMapper = (rs, rowNum) ->
            new PointHistoryEntity(
                    rs.getLong("member_id"),
                    rs.getInt("points_used"),
                    rs.getInt("points_saved"),
                    rs.getLong("member_id")
            );

    public PointHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(PointHistoryEntity pointHistory) {
        String sql = "INSERT INTO point_history (member_id, points_used, points_saved, order_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, pointHistory.getMemberId(), pointHistory.getPointUsed(), pointHistory.getPointSaved(), pointHistory.getOrderId());
    }

    public Optional<PointHistoryEntity> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM point_history WHERE order_id = ? ";
        try{
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, orderId));
        } catch (DataAccessException e){
            return Optional.empty();
        }
    }
}
