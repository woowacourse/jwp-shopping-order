package com.woowahan.techcourse.order.cart;

import com.woowahan.techcourse.cart.application.CartCommandService;
import com.woowahan.techcourse.order.domain.CartItemRemover;
import com.woowahan.techcourse.order.domain.Order;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CartItemRemoverImpl implements CartItemRemover {

    private final CartCommandService cartCommandService;

    public CartItemRemoverImpl(CartCommandService cartCommandService) {
        this.cartCommandService = cartCommandService;
    }

    @Override
    public void removeCartItem(Order order) {
        List<Long> productIds = order.getProductIds();
        cartCommandService.removeAllByProductIds(order.getMemberId(), productIds);
    }
}
