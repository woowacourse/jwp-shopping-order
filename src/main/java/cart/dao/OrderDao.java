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
                .columns("oi.id, oi.product_id, oi.member_id, oi.name, oi.price, oi.image_url, oi.quantity")
                .columns(", m.id, m.email, m.password, m.money, m.point")
                .columns(", o.id, o.created_at, o.total_price, o.use_point")
                .from().table("orders o")
                .innerJoin("order_item oi").on("oi.orders_id = o.id")
                .innerJoin("member m").on("o.member_id = m.id")
                .where().condition("o.id = ?")
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
