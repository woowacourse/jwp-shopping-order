package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("delivery_fee", "coupon_id", "member_id")
                .usingGeneratedKeyColumns("id");
    }

    public void update(final OrderEntity orderEntity) {
        String sql = "UPDATE order SET delivery_fee = ?, coupon_id = ?, member_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderEntity.getDeliveryFee(), orderEntity.getCouponId(), orderEntity.getMemberId(), orderEntity.getId());
    }

    public OrderEntity insert(final OrderEntity orderEntity) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderEntity);
        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new OrderEntity(
                id,
                orderEntity.getDeliveryFee(),
                orderEntity.getCouponId(),
                orderEntity.getMemberId()
        );
    }
}
