package cart.dao;

import cart.dao.entity.OrdersEntity;
import cart.domain.Orders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrdersEntity> ordersEntityRowMapper = (rs,rowNum) -> new OrdersEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("original_price"),
            rs.getInt("discount_price"),
            rs.getBoolean("confirm_state")
    );

    public OrdersDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrders(final long memberId, final int originalPrice, final  int discountPrice){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id",memberId)
                .addValue("original_price",originalPrice)
                .addValue("discount_price",discountPrice)
                .addValue("confirm_state",false);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<OrdersEntity> findAllByMemberId(final long memberId){
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql,ordersEntityRowMapper,memberId);
    }
}
