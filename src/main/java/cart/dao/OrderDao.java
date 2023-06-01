package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public OrderEntity save(final OrderEntity orderEntity) {
        final SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderEntity);
        final long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new OrderEntity(
                savedId,
                orderEntity.getTotalItemPrice(),
                orderEntity.getDiscountedTotalItemPrice(),
                orderEntity.getShippingFee(),
                orderEntity.getOrderedAt(),
                orderEntity.getMemberId()
        );
    }
}
