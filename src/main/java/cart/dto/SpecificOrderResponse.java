package cart.dto;

import cart.domain.Order;
import cart.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class SpecificOrderResponse {

    private List<OrderedProduct> orderedProducts;
    private Integer totalPayment;
    private Integer point;

    public SpecificOrderResponse() {
    }

    public SpecificOrderResponse(final List<OrderedProduct> orderedProducts, final Integer totalPayment, final Integer point) {
        this.orderedProducts = orderedProducts;
        this.totalPayment = totalPayment;
        this.point = point;
    }

    public static SpecificOrderResponse from(final Order order) {
        final List<OrderItem> orderItems = order.getOrderItems();
        final List<OrderedProduct> orderedProducts = orderItems.stream()
                .map(OrderedProduct::from)
                .collect(Collectors.toUnmodifiableList());
        return new SpecificOrderResponse(orderedProducts, order.getPaymentAmount(), order.getPointAmount());
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getTotalPayment() {
        return totalPayment;
    }

    public Integer getPoint() {
        return point;
    }
}
