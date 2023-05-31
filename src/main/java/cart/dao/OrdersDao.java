package cart.dao;

import cart.entity.OrdersEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class OrdersDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<OrdersEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final Long deliveryFee = rs.getLong("delivery_fee");
        final Long couponId = rs.getLong("member_coupon_id");
        final Long memberId = rs.getLong("member_id");

        return new OrdersEntity(id, deliveryFee, couponId, memberId);
    };

    public OrdersDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("delivery_fee", "member_coupon_id", "member_id")
                .usingGeneratedKeyColumns("id");
    }

    public OrdersEntity insert(final OrdersEntity ordersEntity) {
        final SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(ordersEntity);
        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new OrdersEntity(
                id,
                ordersEntity.getDeliveryFee(),
                ordersEntity.getMemberCouponId(),
                ordersEntity.getMemberId()
        );
    }

    public OrdersEntity findByOrderIdAndMemberId(final Long orderId, final Long memberId) {
        String sql = "SELECT * FROM orders WHERE id = ? and member_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, orderId, memberId);
    }

    public List<OrdersEntity> findByMemberId(final Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
