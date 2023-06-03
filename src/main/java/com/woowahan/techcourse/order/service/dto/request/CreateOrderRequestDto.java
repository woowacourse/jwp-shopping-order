package com.woowahan.techcourse.order.service.dto.request;

import com.woowahan.techcourse.order.domain.OrderItem;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateOrderRequestDto {

    @NotNull
    @NotEmpty
    private List<@NotNull CreateOrderCartItemRequestDto> cartItems;
    @NotNull
    private List<@NotNull Long> couponIds;

    private CreateOrderRequestDto() {
    }

    public CreateOrderRequestDto(List<CreateOrderCartItemRequestDto> cartItems, List<Long> couponIds) {
        this.cartItems = cartItems;
        this.couponIds = couponIds;
    }

    public List<CreateOrderCartItemRequestDto> getCartItems() {
        return cartItems;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public static class CreateOrderCartItemRequestDto {

        @NotNull
        private Long id;
        @Positive
        private Integer quantity;
        @NotNull
        private CreateOrderRequestDto.CreateOrderCartItemRequestDto.CreateOrderProductRequestDto product;

        private CreateOrderCartItemRequestDto() {
        }

        public CreateOrderCartItemRequestDto(Long id, Integer quantity, CreateOrderProductRequestDto product) {
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

        public CreateOrderProductRequestDto getProduct() {
            return product;
        }

        public static class CreateOrderProductRequestDto {

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

            public CreateOrderProductRequestDto(Long id, Integer price, String name, String imageUrl) {
                this.id = id;
                this.price = price;
                this.name = name;
                this.imageUrl = imageUrl;
            }

            private CreateOrderProductRequestDto() {
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
