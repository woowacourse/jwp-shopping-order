package cart.dto.order;

import cart.domain.Coupon;
import cart.domain.OrderItem;
import cart.dto.product.ProductResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    @JsonProperty("orderItemId")
    private Long id;
    private ProductResponse product;
    private int quantity;
    private List<Long> couponIds;
    private int total;

    private OrderItemResponse() {
    }

    public OrderItemResponse(
            final Long id,
            final ProductResponse product,
            final int quantity,
            final List<Long> couponIds,
            final int total
    ) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.couponIds = couponIds;
        this.total = total;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        List<Long> couponIds = orderItem.getCoupons()
                .stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        return new OrderItemResponse(
                orderItem.getId(),
                ProductResponse.of(orderItem.getProduct()),
                orderItem.getQuantity(),
                couponIds,
                orderItem.getTotal()
        );
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public int getTotal() {
        return total;
    }
}
