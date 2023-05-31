package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
        return new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getLong("shipping_fee"),
                rs.getLong("total_price"),
                rs.getTimestamp("created_at").toString()
        );
    };

    public long save(final OrderEntity orderEntity) {
        String sql = "INSERT INTO orders (member_id, shipping_fee, total_price) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, orderEntity.getMemberId());
            pst.setLong(2, orderEntity.getShippingFee());
            pst.setLong(3, orderEntity.getTotalPrice());
            return pst;
        }, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        Object id = keys.get("id");
        return ((Number) id).longValue();
    }

    public OrderEntity findById(final long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<OrderEntity> findAllByMemberId(final long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
