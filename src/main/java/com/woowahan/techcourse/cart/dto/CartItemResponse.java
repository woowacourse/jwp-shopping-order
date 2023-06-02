package com.woowahan.techcourse.cart.dto;

import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.product.domain.Product;
import com.woowahan.techcourse.product.ui.dto.ProductResponse;

public class CartItemResponse {

    private final long id;
    private final int quantity;
    private final ProductResponse product;

    private CartItemResponse(long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItem cartItem, Product product) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(product)
        );
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
