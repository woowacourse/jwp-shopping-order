package cart.dao;

import cart.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveOrderProductsByOrderId(Long orderSavedId, List<OrderProductEntity> orderProducts) {
        String sql = "Insert into order_product (name,image_url,price,quantity,order_id) VALUES (?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, orderProducts, orderProducts.size(),
                (PreparedStatement preparedStatement, OrderProductEntity entity) -> {
                    preparedStatement.setString(1, entity.getName());
                    preparedStatement.setString(2, entity.getImage_url());
                    preparedStatement.setInt(3, entity.getPrice());
                    preparedStatement.setInt(4, entity.getQuantity());
                    preparedStatement.setLong(5, orderSavedId);
                });
    }
}
