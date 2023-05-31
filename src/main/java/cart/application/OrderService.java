package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderResponseDto;
import cart.exception.CartItemNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartItemDao cartItemDao;

    public OrderService(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public OrderResponseDto order(final Member member, final OrderRequestDto orderRequestDto) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        checkCartItem(cartItems, orderRequestDto);
        final Order order = new Order(makeOrderProduct(cartItems, orderRequestDto));
        order.applyDeliveryFee(new DeliveryFee(3000));
        return new OrderResponseDto(1L);
    }

    private void checkCartItem(final List<CartItem> cartItems, final OrderRequestDto orderRequestDto) {
        final List<Long> cartItemsId = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
        final boolean allItemExist = cartItemsId.containsAll(orderRequestDto.getCartItems());
        if (!allItemExist) {
            throw new CartItemNotFoundException("주문 요청에 존재하지 않는 카트 아이템이 포함되어 있습니다.");
        }
    }

    private List<OrderProduct> makeOrderProduct(final List<CartItem> cartItems, final OrderRequestDto orderRequestDto) {
        final List<Long> cartItemsId = orderRequestDto.getCartItems();
        return cartItems.stream()
                .filter(cartItem -> cartItemsId.contains(cartItem.getId()))
                .map(OrderProduct::of)
                .collect(Collectors.toList());
    }
}
