package cart.dto;

import cart.domain.Order;
import cart.domain.Price;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final List<OrderCartItemResponse> cartItemsPrice;
    private final DeliveryPriceResponse deliveryPrice;
    private final int discountFromTotalPrice;

    public OrderResponse(List<OrderCartItemResponse> cartItemsPrice, DeliveryPriceResponse deliveryPrice, int discountFromTotalPrice) {
        this.cartItemsPrice = cartItemsPrice;
        this.deliveryPrice = deliveryPrice;
        this.discountFromTotalPrice = discountFromTotalPrice;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getOrderCartItems().stream()
                        .map(OrderCartItemResponse::from)
                        .collect(Collectors.toList()),
                new DeliveryPriceResponse(order.getDeliveryPrice(), new Price(0)),
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
