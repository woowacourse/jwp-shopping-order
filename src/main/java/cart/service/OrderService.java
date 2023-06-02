package cart.service;

import cart.controller.dto.request.OrderRequest;
import cart.controller.dto.response.OrderThumbnailResponse;
import cart.domain.CartItem;
import cart.domain.DiscountPriceCalculator;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.exception.NotOwnerException;
import cart.exception.PaymentAmountNotEqualException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.repository.dto.OrderAndMainProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public long save(final Member member, final OrderRequest request) {
        final List<CartItem> cartItems = cartItemRepository.findByIds(request.getCartItems());
        checkCartItemsOwner(member, cartItems);
        cartItemRepository.deleteAll(request.getCartItems());
        final Order order = createOrder(member, cartItems);
        validatePaymentAmount(order, request.getPaymentAmount());
        return orderRepository.save(order);
    }

    public List<OrderThumbnailResponse> findByMember(final Member member) {
        final List<OrderAndMainProductDto> dtos = orderRepository.findByMember(member);
        return dtos.stream()
                .map(OrderThumbnailResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    private void checkCartItemsOwner(final Member member, final List<CartItem> cartItems) {
        final boolean isIdEquals = cartItems.stream()
                .map(CartItem::getMemberId)
                .allMatch(member::isIdEquals);
        if (isIdEquals) {
            return;
        }
        throw new NotOwnerException();
    }

    private void validatePaymentAmount(final Order order, final int requestPayment) {
        if (order.isPaymentAmountEqual(requestPayment)) {
            return;
        }
        throw new PaymentAmountNotEqualException();
    }

    private Order createOrder(final Member member, final List<CartItem> cartItems) {
        return new Order(
                member.getId(),
                new OrderItems(convertCartItemsToOrderItems(cartItems), new DiscountPriceCalculator())
        );
    }

    private List<OrderItem> convertCartItemsToOrderItems(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toUnmodifiableList());
    }
}
