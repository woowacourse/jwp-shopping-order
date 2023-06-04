package cart.repository.dao;

import cart.repository.entity.OrdersEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdersDao {

    private static final String SELECT_SQL = "SELECT id, original_price, actual_price, delivery_fee, member_id FROM orders ";
    private static final String WHERE_ID = "WHERE id = ? ";
    private static final String WHERE_MEMBER_ID = "WHERE member_id = ? ";

    private static final RowMapper<OrdersEntity> ORDERS_ROW_MAPPER = ((rs, rowNum) ->
            new OrdersEntity(
                    rs.getLong("id"),
                    rs.getInt("original_price"),
                    rs.getInt("actual_price"),
                    rs.getInt("delivery_fee"),
                    rs.getLong("member_id")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrdersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(OrdersEntity ordersEntity) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("original_price", ordersEntity.getOriginalPrice())
                .addValue("actual_price", ordersEntity.getActualPrice())
                .addValue("delivery_fee", ordersEntity.getDeliveryFee())
                .addValue("member_id", ordersEntity.getMemberId());

        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public OrdersEntity findById(Long id) {
        String sql = SELECT_SQL + WHERE_ID;
        return jdbcTemplate.queryForObject(sql, ORDERS_ROW_MAPPER, id);
    }

    public List<OrdersEntity> findAllByMemberId(Long memberId) {
        String sql = SELECT_SQL + WHERE_MEMBER_ID;
        return jdbcTemplate.query(sql, ORDERS_ROW_MAPPER, memberId);
    }

}
