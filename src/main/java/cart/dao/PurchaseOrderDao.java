package cart.dao;

import cart.domain.Member;
import cart.domain.OrderStatus;
import cart.domain.PurchaseOrderInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PurchaseOrderDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final PurchaseOrderItemDao purchaseOrderItemDao;

    public PurchaseOrderDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource, PurchaseOrderItemDao purchaseOrderItemDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("purchase_order")
                .usingGeneratedKeyColumns("id");
        this.purchaseOrderItemDao = purchaseOrderItemDao;
    }

    public Long save(PurchaseOrderInfo purchaseOrderInfo) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", purchaseOrderInfo.getMember().getId())
                .addValue("order_at", purchaseOrderInfo.getOrderAt())
                .addValue("payment", purchaseOrderInfo.getPayment())
                .addValue("used_point", purchaseOrderInfo.getUsedPoint())
                .addValue("status", purchaseOrderInfo.getStatus());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<PurchaseOrderInfo> findAllByMemberId(Long memberId) {
        String sql = "SELECT o.id as order_id, member.id as member_id, member.email as email, member.password as password, o.order_at, o.payment, o.used_point, o.status "
                + "FROM purchase_order AS o "
                + "JOIN member AS member ON o.member_id = member.id "
                + "WHERE member.id = :member_id";
        SqlParameterSource source = new MapSqlParameterSource("member_id", memberId);
        return jdbcTemplate.query(sql, source, getPurchaseOrderRowMapper());
    }

    public PurchaseOrderInfo findById(Long id) {
        String sql = "SELECT o.id as order_id, member.id as member_id, member.email as email, member.password as password, o.order_at, o.payment, o.used_point, o.status "
                + "FROM purchase_order AS o "
                + "JOIN member AS member ON o.member_id = member.id "
                + "WHERE o.id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, source, getPurchaseOrderRowMapper());
    }

    private RowMapper<PurchaseOrderInfo> getPurchaseOrderRowMapper() {
        return (rs, rowNum) ->
                new PurchaseOrderInfo(
                        rs.getLong("order_id"),
                        new Member(rs.getLong("member_id"), rs.getString("email"), rs.getString("password")),
                        rs.getTimestamp("order_at")
                          .toLocalDateTime(),
                        rs.getInt("payment"),
                        rs.getInt("used_point"),
                        OrderStatus.convertTo(rs.getString("status"))
                );
    }

    public void updateStatus(PurchaseOrderInfo purchaseOrderInfo) {
        String sql = "UPDATE purchase_order SET status = :status WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("status", purchaseOrderInfo.getStatus())
                .addValue("id", purchaseOrderInfo.getId());
        jdbcTemplate.update(sql, source);
    }
}
