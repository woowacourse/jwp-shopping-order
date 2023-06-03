package cart.dao.rowmapper;

import cart.dao.entity.OrderEntity;
import cart.domain.member.Member;
import cart.domain.order.OrderHistory;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRowMapper {

    private OrderRowMapper() {
    }

    public static RowMapper<OrderEntity> orderEntity = (rs, rowNum) -> {
        return new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getBigDecimal("total_price"),
                rs.getBigDecimal("use_point"),
                rs.getBigDecimal("use_point"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    };

    public static RowMapper<OrderHistory> orderHistory = (rs, rowNum) -> {
        Member member = new Member(
                rs.getLong("member.id"),
                rs.getString("member.email"),
                rs.getString("member.password"),
                Money.from(rs.getBigDecimal("member.money")),
                Money.from(rs.getBigDecimal("member.point")));

        Long orderId = rs.getLong("orders.id");
        Money totalPrice = Money.from(rs.getBigDecimal("orders.total_price"));
        Money usePoint = Money.from(rs.getBigDecimal("orders.use_point"));
        LocalDateTime createdAt = rs.getTimestamp("orders.created_at").toLocalDateTime();

        List<OrderItem> orderItems = new ArrayList<>();
        do {
            Long orderItemId = rs.getLong("order_item.id");
            Long memberId = rs.getLong("order_item.member_id");
            int orderItemQuantity = rs.getInt("order_item.quantity");

            Product product = new Product(
                    rs.getLong("order_item.product_id"),
                    rs.getString("order_item.name"),
                    rs.getBigDecimal("order_item.price"),
                    rs.getString("order_item.image_url"));

            orderItems.add(new OrderItem(orderItemId, product, Quantity.from(orderItemQuantity), memberId));
        } while (rs.next());

        return new OrderHistory(orderId, member, OrderItems.from(orderItems), totalPrice, usePoint, createdAt);
    };
}
