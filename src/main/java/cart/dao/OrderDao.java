package cart.dao;

import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void save(OrderEntity orderEntity) {
        String sql = "insert into orders(member_id, orders_status_id) values(?, ?)";
        jdbcTemplate.update(sql, orderEntity.getMemberId(), orderEntity.getOrderStatusId());
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
