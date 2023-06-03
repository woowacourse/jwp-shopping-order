package cart.application.dto.order;

import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class FindOrderDetailResponse {

    private final long id;
    private final List<OrderDetailProductResponse> products;
    private final int totalProductPrice;
    private final int discountPrice;
    private final int shippingFee;

    public FindOrderDetailResponse(final long id,
            final List<OrderDetailProductResponse> products, final int totalProductPrice, final int discountPrice,
            final int shippingFee) {
        this.id = id;
        this.products = products;
        this.totalProductPrice = totalProductPrice;
        this.discountPrice = discountPrice;
        this.shippingFee = shippingFee;
    }

    public static FindOrderDetailResponse from(final Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderDetailProductResponse> products = orderItems.stream()
                .map(OrderDetailProductResponse::from)
                .collect(Collectors.toList());
        return new FindOrderDetailResponse(
                order.getId(),
                products,
                order.calculateTotalProductPrice(),
                order.calculateDiscountPrice(),
                order.getShippingFee().getValue()
        );
    }

    public long getId() {
        return id;
    }

    public List<OrderDetailProductResponse> getProducts() {
        return products;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }
}
