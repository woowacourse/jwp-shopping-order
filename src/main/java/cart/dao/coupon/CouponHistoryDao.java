package cart.dao.coupon;

import cart.entity.coupon.CouponHistoryEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CouponHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public CouponHistoryDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CouponHistoryEntity> rowMapper = (rs, rowNum) ->
            new CouponHistoryEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("order_table_id")
            );

    public void saveAll(final List<CouponHistoryEntity> historyEntities) {
        String sql = "INSERT INTO coupon_history (id, name, order_table_id) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CouponHistoryEntity entity = historyEntities.get(i);
                ps.setLong(1, entity.getId());
                ps.setString(2, entity.getName());
                ps.setLong(3, entity.getOrderTableId());
            }

            @Override
            public int getBatchSize() {
                return historyEntities.size();
            }
        });
    }

    public List<CouponHistoryEntity> findAllByOrderId(final Long orderId) {
        String sql = "SELECT * FROM coupon_history WHERE order_table_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
