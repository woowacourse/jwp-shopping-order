package cart.dao;

import cart.domain.Member;
import cart.domain.OrderCartItem;
import cart.domain.OrderCartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class DbOrderCartItemDao implements OrderCartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    private final RowMapper<OrderCartItemEntity> rowMapper = ((rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long orderId = rs.getLong("ordered_id");
        String name = rs.getString("name");
        int quantity = rs.getInt("quantity");
        String imageUrl = rs.getString("image_url");
        int totalPrice = rs.getInt("total_price");
        int totalDiscountPrice = rs.getInt("total_discount_price");
        return new OrderCartItemEntity(id, orderId, name, quantity, imageUrl, totalPrice, totalDiscountPrice);
    });

    public DbOrderCartItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("ordered_item")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long insert(OrderCartItemEntity orderCartItemEntity) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("ordered_id", orderCartItemEntity.getOrderedId());
        params.put("name", orderCartItemEntity.getName());
        params.put("image_url", orderCartItemEntity.getImageUrl());
        params.put("quantity", orderCartItemEntity.getQuantity());
        params.put("total_price", orderCartItemEntity.getTotalPrice());
        params.put("total_discount_price", orderCartItemEntity.getTotalDiscountPrice());
        return insertActor.executeAndReturnKey(params).longValue();
    }

    @Override
    public OrderCartItemEntity findById(Long id) {
        String sql = "select * from ordered_item where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<OrderCartItemEntity> findByOrderId(Long orderId) {
        String sql = "select * from ordered_item where ordered_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
