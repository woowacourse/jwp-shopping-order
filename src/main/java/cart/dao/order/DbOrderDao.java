package cart.dao.order;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DbOrderDao implements OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long memberId = rs.getLong("member_id");
        Timestamp time = rs.getTimestamp("time");
        int deliveryPrice = rs.getInt("delivery_price");
        int discountPriceFromTotal = rs.getInt("discount_price_from_total");
        return new OrderEntity(id, memberId, time, deliveryPrice, discountPriceFromTotal);
    };

    public DbOrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("ordered")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long insert(OrderEntity orderEntity) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("member_id", orderEntity.getMemberId());
        params.put("time", orderEntity.getTime());
        params.put("delivery_price", orderEntity.getDeliveryPrice());
        params.put("discount_price_from_total", orderEntity.getDiscountFromTotal());
        return insertActor.executeAndReturnKey(params).longValue();
    }

    @Override
    public OrderEntity findById(Long id) {
        String sql = "select * from ordered where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<OrderEntity> findAll(Long memberId) {
        String sql = "select * from ordered where member_id = ?";
        return jdbcTemplate.query(sql,rowMapper,memberId);
    }


}
