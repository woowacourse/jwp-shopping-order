package cart.domain.order.validator;

import java.util.List;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.cartitem.domain.CartItems;
import cart.domain.order.domain.dto.OrderCartItemDto;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validate(CartItems cartItems, List<OrderCartItemDto> orderCartItemDtos) {
        for (OrderCartItemDto orderCartItemDto : orderCartItemDtos) {
            CartItem findCartItem = cartItems.getCartItemById(orderCartItemDto.getCartItemId());
            validateCartItemInfoMatch(findCartItem, orderCartItemDto);
        }
    }

    private void validateCartItemInfoMatch(CartItem findCartItem, OrderCartItemDto orderCartItemDto) {
        String orderCartItemName = orderCartItemDto.getOrderCartItemName();
        int orderCartItemPrice = orderCartItemDto.getOrderCartItemPrice();
        String orderCartItemImageUrl = orderCartItemDto.getOrderCartItemImageUrl();
        if (findCartItem.isNotSameProductInfo(orderCartItemName, orderCartItemPrice, orderCartItemImageUrl)) {
            throw new IllegalArgumentException("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }
    }
}
