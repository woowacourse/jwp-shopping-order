package cart.dao;

import cart.domain.OrderItem;
import cart.entity.CartItemEntity;
import cart.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderItemDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) -> new OrderItemEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("image_url"),
            rs.getLong("price"),
            rs.getInt("quantity"),
            rs.getLong("order_id")
    );

    public OrderItemDao(final DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingColumns("name", "image_url", "price", "quantity", "order_id")
                .usingGeneratedKeyColumns("id");
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public OrderItemEntity insert(final OrderItemEntity orderItemEntity) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderItemEntity);
        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new OrderItemEntity(
                id,
                orderItemEntity.getName(),
                orderItemEntity.getImageUrl(),
                orderItemEntity.getPrice(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getOrderId()
        );
    }

    public void update(final OrderItemEntity orderItemEntity) {
        String sql = "UPDATE order_item SET name = :name, image_url = :imageUrl, price = price, quantity = quantity, order_id = :orderId";
        final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", orderItemEntity.getName());
        sqlParameterSource.addValue("image_url", orderItemEntity.getImageUrl());
        sqlParameterSource.addValue("price", orderItemEntity.getPrice());
        sqlParameterSource.addValue("quantity", orderItemEntity.getQuantity());
        sqlParameterSource.addValue("order_id", orderItemEntity.getOrderId());

        namedJdbcTemplate.update(sql, sqlParameterSource);
    }

    public List<OrderItemEntity> findByOrderId(final Long orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = :id";
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", orderId);
        return namedJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
    }
}
