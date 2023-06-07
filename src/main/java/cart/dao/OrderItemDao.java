package cart.dao;

import cart.dao.entity.OrderItemEntity;
import cart.dao.rowmapper.OrderItemRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cart.dao.support.SqlHelper.sqlHelper;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long insertOrderItem(OrderItemEntity orderItemEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                        .addValue("orders_id", orderItemEntity.getOrderId())
                        .addValue("member_id", orderItemEntity.getMemberId())
                        .addValue("product_id", orderItemEntity.getProductId())
                        .addValue("name", orderItemEntity.getName())
                        .addValue("price", orderItemEntity.getPrice())
                        .addValue("image_url", orderItemEntity.getImageUrl())
                        .addValue("quantity", orderItemEntity.getQuantity())
                )
                .longValue();
    }

    public void insertBatch(List<OrderItemEntity> orderItemEntities) {
        String sql = sqlHelper()
                .insert().table("order_item")
                .columns("(orders_id, member_id, product_id, name, price, image_url, quantity)")
                .values("?, ?, ?, ?, ?, ?, ?")
                .toString();

        jdbcTemplate.batchUpdate(sql, orderItemEntities, orderItemEntities.size(), (ps, arg) -> {
            ps.setLong(1, arg.getOrderId());
            ps.setLong(2, arg.getMemberId());
            ps.setLong(3, arg.getProductId());
            ps.setString(4, arg.getName());
            ps.setInt(5, arg.getPrice().intValue());
            ps.setString(6, arg.getImageUrl());
            ps.setInt(7, arg.getQuantity());
        });
    }

    public List<OrderItemEntity> getByOrderId(Long orderId) {
        String sql = sqlHelper()
                .select().columns("id, orders_id, member_id, product_id, name, price, image_url, quantity")
                .from().table("order_item")
                .where().condition("orders_id = ?")
                .toString();

        return jdbcTemplate.query(sql, OrderItemRowMapper.orderItemEntity, orderId);
    }
}
