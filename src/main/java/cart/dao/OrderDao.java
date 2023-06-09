package cart.dao;

import cart.entity.OrderEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderEntity> orderItemRowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getInt("total_item_price"),
                    rs.getInt("discounted_total_item_price"),
                    rs.getInt("shipping_fee"),
                    rs.getTimestamp("ordered_at").toLocalDateTime(),
                    rs.getLong("member_id")
            );

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

    public List<OrderEntity> findAllByMemberId(final Long memberId) {
        final String sql = "select * from orders where member_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, memberId);
    }

    public OrderEntity findById(final Long id) {
        final String sql = "select * from orders where id = ?";
        return jdbcTemplate.queryForObject(sql, orderItemRowMapper, id);
    }
}
