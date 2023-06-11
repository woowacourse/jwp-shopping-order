package cart.dao;

import cart.domain.Product;
import cart.domain.purchaseorder.PurchaseOrderItem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PurchaseOrderItemDao {

    private final RowMapper<PurchaseOrderItem> rowMapper = (rs, rowNum) ->
            new PurchaseOrderItem(
                    rs.getLong("order_id"),
                    new Product(rs.getLong("p_id"), rs.getString("name"),
                            rs.getInt("price"), rs.getString("image_url")),
                    rs.getInt("quantity")
            );

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public PurchaseOrderItemDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("purchase_order_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(PurchaseOrderItem purchaseOrderItem, Long purchaseOrderId) {
        Product product = purchaseOrderItem.getProduct();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("order_id", purchaseOrderId)
                .addValue("product_id", product.getId())
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl())
                .addValue("quantity", purchaseOrderItem.getQuantity());
        return insertAction.executeAndReturnKey(params)
                           .longValue();
    }

    public void saveAll(List<PurchaseOrderItem> purchaseOrderItems, Long purchaseOrderId) {
        String sql = "INSERT INTO purchase_order_item (order_id, product_id, name, price, image_url, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        PurchaseOrderItem purchaseOrderItem = purchaseOrderItems.get(i);
                        ps.setLong(1, purchaseOrderId);
                        Product product = purchaseOrderItem.getProduct();
                        ps.setLong(2, product.getId());
                        ps.setString(3, product.getName());
                        ps.setInt(4, product.getPrice());
                        ps.setString(5, product.getImageUrl());
                        ps.setInt(6, purchaseOrderItem.getQuantity());
                    }

                    @Override
                    public int getBatchSize() {
                        return purchaseOrderItems.size();
                    }
                }
        );
    }

    public List<PurchaseOrderItem> findAllByPurchaseOrderId(Long order_id) {
        String sql = "SELECT oi.id as order_id, p.id as p_id, p.name as name, p.price as price, p.image_url as image_url, oi.quantity "
                + "FROM purchase_order_item AS oi "
                + "JOIN product AS p ON oi.product_id = p.id "
                + "WHERE oi.order_id = :order_id";
        SqlParameterSource source = new MapSqlParameterSource("order_id", order_id);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }
}
