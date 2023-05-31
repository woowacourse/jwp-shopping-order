package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
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
        checkCartItem(member, orderRequestDto);
        return new OrderResponseDto(1L);
    }

    private void checkCartItem(final Member member, final OrderRequestDto orderRequestDto) {
        final List<CartItem> byMemberId = cartItemDao.findByMemberId(member.getId());
        final List<Long> cartItems = byMemberId.stream().map(CartItem::getId).collect(Collectors.toList());
        final boolean allItemExist = cartItems.containsAll(orderRequestDto.getCartItems());
        if (!allItemExist) {
            throw new CartItemNotFoundException("주문 요청에 존재하지 않는 카트 아이템이 포함되어 있습니다.");
        }
    }
}
