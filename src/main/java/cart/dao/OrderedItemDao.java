package cart.dao;

import cart.entity.OrderedItemEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderedItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderedItemEntity> rowMapper = (rs, rowNum) ->
            new OrderedItemEntity(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("shopping_order_id"),
                    rs.getString("product_name_at_order"),
                    rs.getInt("product_price_at_order"),
                    rs.getString("product_image_url_at_order"),
                    rs.getInt("quantity")
            );

    public OrderedItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchSave(List<OrderedItemEntity> orderedItemEntities) {
        final String sql = "INSERT INTO ordered_item "
                + "(`product_id`, `shopping_order_id`, `product_name_at_order`, `product_price_at_order`, `product_image_url_at_order`, `quantity`)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, orderedItemEntities.get(i).getProductId());
                ps.setLong(2, orderedItemEntities.get(i).getShoppingOrderId());
                ps.setString(3, orderedItemEntities.get(i).getProductNameAtOrder());
                ps.setInt(4, orderedItemEntities.get(i).getProductPriceAtOrder());
                ps.setString(5, orderedItemEntities.get(i).getProductImageUrlAtOrder());
                ps.setInt(6, orderedItemEntities.get(i).getQuantity());
            }

            @Override
            public int getBatchSize() {
                return orderedItemEntities.size();
            }
        });
    }
}
