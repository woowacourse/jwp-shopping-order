package com.woowahan.techcourse.order.service.dto.request;

import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderCoupon;
import com.woowahan.techcourse.order.domain.OrderItem;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateOrderRequestDto {

    @NotNull
    @NotEmpty
    private List<@NotNull CreateOrderCartItem> cartItems;
    @NotNull
    private List<@NotNull Long> couponIds;

    private CreateOrderRequestDto() {
    }

    public CreateOrderRequestDto(List<CreateOrderCartItem> cartItems, List<Long> couponIds) {
        this.cartItems = cartItems;
        this.couponIds = couponIds;
    }

    public List<CreateOrderCartItem> getCartItems() {
        return cartItems;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public Order toOrder(long memberId) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(CreateOrderCartItem::toOrderItem)
                .collect(Collectors.toList());
        List<OrderCoupon> orderCoupons = couponIds.stream()
                .map(OrderCoupon::new)
                .collect(Collectors.toList());
        return new Order(memberId, orderItems, orderCoupons);
    }

    public static class CreateOrderCartItem {

        @NotNull
        private Long id;
        @Positive
        private Integer quantity;
        @NotNull
        private CreateOrderProduct product;

        private CreateOrderCartItem() {
        }

        public CreateOrderCartItem(Long id, Integer quantity, CreateOrderProduct product) {
            this.id = id;
            this.quantity = quantity;
            this.product = product;
        }

        public OrderItem toOrderItem() {
            return new OrderItem(id, quantity, product.getId(), product.getPrice(),
                    product.getName(), product.getImageUrl());
        }

        public Long getId() {
            return id;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public CreateOrderProduct getProduct() {
            return product;
        }

        public static class CreateOrderProduct {

            @NotNull
            private Long id;
            @NotNull
            @Min(0)
            private Integer price;
            @NotNull
            @NotEmpty
            private String name;
            @NotNull
            @NotEmpty
            private String imageUrl;

            public CreateOrderProduct(Long id, Integer price, String name, String imageUrl) {
                this.id = id;
                this.price = price;
                this.name = name;
                this.imageUrl = imageUrl;
            }

            private CreateOrderProduct() {
            }

            public Long getId() {
                return id;
            }

            public Integer getPrice() {
                return price;
            }

            public String getName() {
                return name;
            }

            public String getImageUrl() {
                return imageUrl;
            }
        }
    }
}
