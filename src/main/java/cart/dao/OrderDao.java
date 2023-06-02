 package cart.dao;

import cart.dao.entity.OrderEntity;
import cart.dao.rowmapper.OrderRowMapper;
import cart.domain.order.OrderHistory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.dao.support.SqlHelper.sqlHelper;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long insertOrder(OrderEntity orderEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                        .addValue("member_id", orderEntity.getMemberId())
                        .addValue("total_price", orderEntity.getTotalPrice())
                        .addValue("use_point", orderEntity.getUsePoint())
                        .addValue("created_at", orderEntity.getCreatedAt()))
                .longValue();
    }

    public Optional<OrderHistory> getByOrderId(Long orderId) {
        String sql = sqlHelper()
                .select()
                .columns("order_item.id, order_item.product_id, order_item.member_id, order_item.name, order_item.price, order_item.image_url, order_item.quantity")
                .columns(", member.id, member.email, member.password, member.money, member.point")
                .columns(", orders.id, orders.created_at, orders.total_price, orders.use_point")
                .from().table("orders")
                .innerJoin("order_item").on("order_item.orders_id = orders.id")
                .innerJoin("member").on("orders.member_id = member.id")
                .where().condition("orders.id = ?")
                .toString();

        try {
            OrderHistory orderHistory = jdbcTemplate.queryForObject(sql, OrderRowMapper.orderHistory, orderId);
            return Optional.ofNullable(orderHistory);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<OrderHistory> getAllByMemberId(Long memberId) {
        return getByMemberId(memberId).stream()
                .map(OrderEntity::getId)
                .map(this::getByOrderId)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private List<OrderEntity> getByMemberId(Long memberId) {
        String sql = sqlHelper()
                .select().columns("id, member_id, total_price, use_point, created_at")
                .from().table("orders")
                .where().condition("member_id = ?")
                .toString();

        return jdbcTemplate.query(sql, OrderRowMapper.orderEntity, memberId);
    }
}
