package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return jdbcTemplate.update(sql, orderEntity.getMemberId(), orderEntity.getShippingFee(), orderEntity.getTotalPrice());
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
