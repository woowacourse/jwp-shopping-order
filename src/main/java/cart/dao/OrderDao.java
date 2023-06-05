package cart.dao;

import cart.domain.OrderStatus;
import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public OrderEntity findBy(Long memberId, Long orderId) {
        String sql = "select id, member_id, orders_status_id, create_at from orders where member_id = ? and id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), memberId, orderId);
    }

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql = "select id, member_id, orders_status_id, create_at from orders where member_id = ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), memberId);
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

        return Long.parseLong(String.valueOf(keyHolder.getKey()));
    }

    public void delete(Long memberId, Long orderId) {
        String sql = "update orders set orders_status_id = ? where member_id = ? and id = ?";

        jdbcTemplate.update(sql, OrderStatus.CANCELLED.getOrderStatusId(), memberId, orderId);
    }

    private static class OrderRowMapper implements RowMapper<OrderEntity> {

        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            long memberId = rs.getLong("member_id");
            int orderStatusId = rs.getInt("orders_status_id");
            LocalDate createAt = rs.getTimestamp("create_at").toLocalDateTime().toLocalDate();

            return new OrderEntity(id, memberId, orderStatusId, createAt);
        }
    }
}
