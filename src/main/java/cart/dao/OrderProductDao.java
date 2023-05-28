package cart.dao;

import cart.domain.orderproduct.OrderProduct;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrderProductDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_product")
                .usingColumns("order_id", "product_id", "product_name", "product_price", "product_image_url", "quantity")
                .usingGeneratedKeyColumns("id");
    }

    public void save(final List<OrderProduct> orderProducts) {
        final String sql = "INSERT INTO order_product(order_id, product_id, product_name, product_price, product_image_url, quantity) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final OrderProduct orderProduct = orderProducts.get(i);
                ps.setLong(1, orderProduct.getOrderId());
                ps.setLong(2, orderProduct.getProductId());
                ps.setString(3, orderProduct.getProductNameValue());
                ps.setInt(4, orderProduct.getProductPriceValue());
                ps.setString(5, orderProduct.getProductImageUrlValue());
                ps.setInt(6, orderProduct.getQuantityValue());
            }

            @Override
            public int getBatchSize() {
                return orderProducts.size();
            }
        });
    }
}
