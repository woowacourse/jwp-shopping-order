package cart.persistence.repository;

import cart.application.domain.OrderInfo;
import cart.application.domain.OrderInfos;
import cart.application.domain.Product;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class OrderInfosPersistenceAdapter {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderInfosPersistenceAdapter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void insert(OrderInfos orderInfos, Long orderId) {
        String sql = "INSERT INTO order_info (order_id, product_id, name, price, image_url, quantity) " +
                "VALUES (:order_id, :product_id, :name, :price, :image_url, :quantity)";
        List<SqlParameterSource> namedParameters = new ArrayList<>();

        for (OrderInfo info : orderInfos.getValues()) {
            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("order_id", orderId)
                    .addValue("product_id", info.getProduct().getId())
                    .addValue("name", info.getName())
                    .addValue("price", info.getPrice())
                    .addValue("image_url", info.getImageUrl())
                    .addValue("quantity", info.getQuantity());
            namedParameters.add(parameters);
        }
        namedParameterJdbcTemplate.batchUpdate(sql, namedParameters.toArray(SqlParameterSource[]::new));
    }

    public OrderInfos findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_info " +
                "INNER JOIN product ON order_info.product_id = product.id " +
                "WHERE order_info.order_id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", orderId);
        List<OrderInfo> orderInfos = namedParameterJdbcTemplate.query(sql, sqlParameterSource, (rs, rowNum) ->
                new OrderInfo(rs.getLong("id"),
                        extractProduct(rs),
                        rs.getString("order_info.name"),
                        rs.getInt("order_info.price"),
                        rs.getString("order_info.image_url"),
                        rs.getInt("order_info.quantity")
                )
        );
        return new OrderInfos(orderInfos);
    }

    private Product extractProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getLong("product.id"),
                rs.getString("product.name"),
                rs.getInt("product.price"),
                rs.getString("product.image_url"),
                rs.getDouble("product.point_ratio"),
                rs.getBoolean("product.point_available")
        );
    }
}
