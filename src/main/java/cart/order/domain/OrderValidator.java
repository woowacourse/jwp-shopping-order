package cart.order.domain;

import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;
import static cart.order.exception.OrderExceptionType.NO_AUTHORITY_ORDER_ITEM;

import cart.cartitem.domain.CartItem;
import cart.order.exception.OrderException;
import cart.product.domain.Product;

public class OrderValidator {

    public void validate(Long memberId, CartItem cartItem, Product product) {
        if (!cartItem.matches(product)) {
            throw new OrderException(MISMATCH_PRODUCT);
        }
        if (!cartItem.getMemberId().equals(memberId)) {
            throw new OrderException(NO_AUTHORITY_ORDER_ITEM);
        }
    }
}
