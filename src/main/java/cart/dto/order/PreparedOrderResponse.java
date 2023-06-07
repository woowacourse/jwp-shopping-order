package cart.dto.order;

import cart.domain.order.Order;
import cart.domain.value.Price;
import cart.dto.DeliveryPriceResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PreparedOrderResponse {
    private final List<OrderCartItemResponse> cartItemsPrice;
    private final DeliveryPriceResponse deliveryPrice;
    private final int discountFromTotalPrice;

    public PreparedOrderResponse(List<OrderCartItemResponse> cartItemsPrice, DeliveryPriceResponse deliveryPrice, int discountFromTotalPrice) {
        this.cartItemsPrice = cartItemsPrice;
        this.deliveryPrice = deliveryPrice;
        this.discountFromTotalPrice = discountFromTotalPrice;
    }

    public static PreparedOrderResponse from(Order order) {
        return new PreparedOrderResponse(
                order.getOrderCartItems().stream()
                        .map(OrderCartItemResponse::from)
                        .collect(Collectors.toList()),
                new DeliveryPriceResponse(order.getDeliveryPrice(), order.getDisountDeilveryPrice()),
                order.getDiscountPriceFromTotal().getValue()
        );
    }

    public List<OrderCartItemResponse> getCartItemsPrice() {
        return cartItemsPrice;
    }

    public DeliveryPriceResponse getDeliveryPrice() {
        return deliveryPrice;
    }

    public int getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }
}
