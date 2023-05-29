package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final long deliveryFee = rs.getLong("delivery_fee");
        final Long couponId = rs.getLong("coupon_id");
        final Long memberId = rs.getLong("member_id");
        return new OrderEntity(id, deliveryFee, couponId, memberId);
    };

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("delivery_fee", "coupon_id", "member_id")
                .usingGeneratedKeyColumns("id");
    }

    public void update(final OrderEntity orderEntity) {
        String sql = "UPDATE orders SET delivery_fee = ?, coupon_id = ?, member_id = ? WHERE id = ?";
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

    public List<OrderEntity> findByMemberId(final Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<OrderEntity> findById(final Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
