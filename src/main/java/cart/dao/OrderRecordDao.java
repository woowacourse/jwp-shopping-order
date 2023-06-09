package cart.dao;

import cart.dao.entity.OrderRecordEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderRecordDao {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public OrderRecordDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("order_record")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final OrderRecordEntity orderRecordEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", orderRecordEntity.getMemberId());
        params.put("order_time", orderRecordEntity.getOrderTime());
        return this.simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderRecordEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM order_record WHERE member_id = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new OrderRecordEntity(rs.getLong("id"), rs.getLong("member_id"),
                rs.getTimestamp("order_time"));
        }, memberId);
    }

    public OrderRecordEntity findById(final Long orderId) {
        final String sql = "SELECT * FROM order_record WHERE id = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new OrderRecordEntity(rs.getLong("id"), rs.getLong("member_id"),
                rs.getTimestamp("order_time"));
        }, orderId);
    }
}
