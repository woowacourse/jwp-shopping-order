package cart.dao;

import cart.entity.OrderDetailEntity;
import cart.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert orderSimpleInsert;
    private final SimpleJdbcInsert orderDetailSimpleInsert;
    private final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("payment"),
                    rs.getInt("discount_point")
            );
    private final RowMapper<OrderDetailEntity> orderDetailEntityRowMapper = (rs, rowNum) ->
            new OrderDetailEntity(
                    rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity")
            );

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderSimpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "payment", "discount_point");
        this.orderDetailSimpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_detail")
                .usingGeneratedKeyColumns("id");
    }

    public Long addOrder(final OrderEntity orderEntity) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", orderEntity.getMemberId())
                .addValue("payment", orderEntity.getPayment())
                .addValue("discount_point", orderEntity.getDiscountPoint());

        return orderSimpleInsert.executeAndReturnKey(params).longValue();
    }

    public Long addOrderDetail(final OrderDetailEntity orderDetailEntity) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("order_id", orderDetailEntity.getOrderId())
                .addValue("product_id", orderDetailEntity.getProductId())
                .addValue("quantity", orderDetailEntity.getQuantity());

        return orderDetailSimpleInsert.executeAndReturnKey(params).longValue();
    }

    public OrderEntity getOrderEntityById(final Long id) {
        try {
            final String sql = "SELECT * FROM orders WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, orderEntityRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("id : " + id + "인 주문 정보가 존재하지 않습니다.");
        }
    }

    public List<OrderEntity> getOrderEntityByMemberId(final Long memberId, final int start, final int size) {
        final String sql = "SELECT * FROM orders WHERE member_id = ? " +
                "ORDER BY ID " +
                "LIMIT " + size + " OFFSET " + start;
        return jdbcTemplate.query(sql, orderEntityRowMapper, memberId);
    }

    public List<OrderDetailEntity> getOrderDetailEntitiesByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_detail WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderDetailEntityRowMapper, orderId);
    }

    public List<OrderEntity> getAllOrders() {
        final String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, orderEntityRowMapper);
    }

    public Integer countAllOrders() {
        final String sql = "SELECT COUNT(*) FROM orders";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
