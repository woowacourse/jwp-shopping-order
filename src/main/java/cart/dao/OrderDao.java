package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(final OrderEntity orderEntity) {
        String sql = "INSERT INTO orders (member_id, shipping_fee, total_price) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, orderEntity.getMemberId(), orderEntity.getShippingFee(), orderEntity.getTotalPrice());
    }
}
