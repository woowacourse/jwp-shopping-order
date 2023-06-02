package cart.dao;

import cart.domain.OrderStatus;
import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public OrderEntity findById(Long orderId) {
        String sql = "select id, member_id, orders_status_id, create_at from orders where id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), orderId);
    }

    public Long save(OrderEntity orderEntity) {
        String sql = "insert into orders(member_id, orders_status_id) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, orderEntity.getMemberId());
            ps.setInt(2, orderEntity.getOrderStatusId());
            return ps;
        }, keyHolder);

        return (Long) keyHolder.getKeys().get("ID");
    }

    public void deleteById(Long orderId) {
        String sql = "update orders set orders_status_id = ? where id = ?";

        jdbcTemplate.update(sql, OrderStatus.CANCELLED.getOrderStatusId(), orderId);
    }

    private static class OrderRowMapper implements RowMapper<OrderEntity> {

        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            long memberId = rs.getLong("member_id");
            int orderStatusId = rs.getInt("orders_status_id");
            Date createAt = rs.getDate("create_at");

            return new OrderEntity(id, memberId, orderStatusId, createAt);
        }
    }
}
