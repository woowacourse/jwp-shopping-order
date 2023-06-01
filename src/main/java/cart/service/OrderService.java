package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.exception.IncorrectPriceException;
import cart.exception.NonExistCartItemException;
import cart.exception.NonExistOrderException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public Long register(OrderRequest orderRequest, Member member) {
        List<CartItem> cartItems = orderRequest.getCartItemIds().stream()
                .map(this::getCartItem)
                .collect(Collectors.toList());

        Order order = Order.of(member, cartItems, orderRequest.getDeliveryFee());

        Money totalPrice = order.calculateTotalPrice();
        if (totalPrice.isNotSameValue(orderRequest.getTotalOrderPrice())) {
            throw new IncorrectPriceException();
        }

        Order savedOrder = orderRepository.save(order);
        deleteOrdered(cartItems);
        return savedOrder.getId();
    }

    private void deleteOrdered(List<CartItem> cartItems) {
        cartItems.stream()
                .map(CartItem::getId)
                .forEach(cartItemRepository::deleteById);
    }

    private CartItem getCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(NonExistCartItemException::new);
    }

    public OrderDetailResponse findById(Long id, Member member) {
        Order order = orderRepository.findById(id)
                .orElseThrow(NonExistOrderException::new);
        order.checkOwner(member);
        return OrderDetailResponse.from(order);
    }
}
