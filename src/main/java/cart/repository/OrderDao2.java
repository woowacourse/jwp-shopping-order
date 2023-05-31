package cart.repository;

import cart.dao.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

@Repository
public class OrderDao2 {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao2(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(final OrderEntity orderEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("member_id", orderEntity.getMemberId())
                .addValue("coupon_id", orderEntity.getCouponId())
                .addValue("shipping_fee", orderEntity.getShippingFee())
                .addValue("total_price", orderEntity.getTotalPrice())
        ).longValue();
    }
}
