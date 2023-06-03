package com.woowahan.techcourse.order.ui.dto.response;

import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final long id;
    private final BigDecimal originalPrice;
    private final BigDecimal actualPrice;
    private final List<OrderItemDto> cartItems;

    public OrderResponse(long id, BigDecimal originalPrice, BigDecimal actualPrice, List<OrderItemDto> cartItems) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
        this.cartItems = cartItems;
    }

    public static OrderResponse from(Order orderResult) {
        return new OrderResponse(
                orderResult.getOrderId(),
                orderResult.getOriginalPrice(),
                orderResult.getActualPrice(),
                OrderItemDto.from(orderResult.getOrderItems())
        );
    }

    public long getId() {
        return id;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public List<OrderItemDto> getCartItems() {
        return cartItems;
    }

    private static class OrderItemDto {

        private final long id;
        private final long quantity;
        private final ProductDto product;

        private OrderItemDto(long id, long quantity, ProductDto product) {
            this.id = id;
            this.quantity = quantity;
            this.product = product;
        }

        public static List<OrderItemDto> from(List<OrderItem> orderItems) {
            return orderItems.stream()
                    .map(OrderItemDto::from)
                    .collect(Collectors.toList());
        }

        private static OrderItemDto from(OrderItem orderItem) {
            return new OrderItemDto(
                    orderItem.getCartItemId(),
                    orderItem.getQuantity(),
                    new ProductDto(
                            orderItem.getProductId(),
                            orderItem.getName(),
                            orderItem.getPrice(),
                            orderItem.getImageUrl()
                    )
            );
        }

        public long getId() {
            return id;
        }

        public long getQuantity() {
            return quantity;
        }

        public ProductDto getProduct() {
            return product;
        }
    }

    private static class ProductDto {

        private final long id;
        private final String name;
        private final long price;
        private final String imageUrl;

        private ProductDto(long id, String name, long price, String imageUrl) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
