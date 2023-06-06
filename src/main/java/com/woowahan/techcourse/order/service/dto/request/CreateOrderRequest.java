package com.woowahan.techcourse.order.service.dto.request;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateOrderRequest {

    @NotNull
    @NotEmpty
    private List<@NotNull CreateOrderCartItemRequest> cartItems;
    private List<@NotNull Long> couponIds = new ArrayList<>();

    private CreateOrderRequest() {
    }

    public CreateOrderRequest(List<CreateOrderCartItemRequest> cartItems, List<Long> couponIds) {
        this.cartItems = cartItems;
        this.couponIds = couponIds;
    }

    public List<CreateOrderCartItemRequest> getCartItems() {
        return cartItems;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public static class CreateOrderCartItemRequest {

        @NotNull
        private Long id;
        @Positive
        private Integer quantity;
        @NotNull
        private CreateOrderRequest.CreateOrderCartItemRequest.CreateOrderProductRequest product;

        private CreateOrderCartItemRequest() {
        }

        public CreateOrderCartItemRequest(Long id, Integer quantity, CreateOrderProductRequest product) {
            this.id = id;
            this.quantity = quantity;
            this.product = product;
        }

        public Long getId() {
            return id;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public CreateOrderProductRequest getProduct() {
            return product;
        }

        public static class CreateOrderProductRequest {

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

            public CreateOrderProductRequest(Long id, Integer price, String name, String imageUrl) {
                this.id = id;
                this.price = price;
                this.name = name;
                this.imageUrl = imageUrl;
            }

            private CreateOrderProductRequest() {
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
