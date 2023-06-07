package cart.dao.rowmapper;

import cart.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.RowMapper;

public final class OrderItemRowMapper {

    private OrderItemRowMapper() {
    }

    public static RowMapper<OrderItemEntity> orderItemEntity = (rs, rowNum) -> {
        return new OrderItemEntity(
                rs.getLong("id"),
                rs.getLong("orders_id"),
                rs.getLong("member_id"),
                rs.getLong("product_id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getString("image_url"),
                rs.getInt("quantity")
        );
    };
}
