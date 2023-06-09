package cart.application;

import cart.domain.*;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Order createDraftOrder(final Member member, final List<Long> cartItemIds) {
        if (cartItemIds.isEmpty()) {
            throw new OrderException.EmptyItemInputException();
        }
        final List<OrderItem> orderItems = cartItemIds.stream()
                .map(cartItemId -> this.findCartItemOf(cartItemId, member))
                .map(OrderItem::from)
                .collect(Collectors.toList());
        return new Order(member, orderItems);
    }

    private CartItem findCartItemOf(final Long cartItemId, final Member member) {
        final CartItem cartItem = this.cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException.NotFoundException(cartItemId));

        if (!member.equals(cartItem.getMember())) {
            throw new CartItemException.IllegalMemberException(cartItem, member);
        }

        return cartItem;
    }

    public Long createOrderAndSave(final Member member, final List<Long> cartItemIds) {
        final Order draftOrder = this.createDraftOrder(member, cartItemIds);
        final Long orderId = this.orderRepository.create(draftOrder);
        this.checkPriceMatch(cartItemIds, draftOrder);
        cartItemIds.forEach(this.cartItemRepository::deleteById);

        return orderId;
    }

    private void checkPriceMatch(final List<Long> cartItemIds, final Order draftOrder) {
        final Money currentPrice = this.convertCartItemsToPrice(cartItemIds);
        final Money orderPrice = draftOrder.calculateOriginalTotalPrice();
        if (!currentPrice.equals(orderPrice)) {
            throw new OrderException.PriceMismatchException(orderPrice, currentPrice);
        }
    }

    private Money convertCartItemsToPrice(final List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(this.cartItemRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(CartItem::getProduct)
                .map(Product::getPrice)
                .map(Money::from)
                .reduce(Money.from(0), Money::add);
    }

    @Transactional(readOnly = true)
    public Order retrieveOrderById(final Long orderId) {
        return this.orderRepository.findById(orderId).orElseThrow(() -> new OrderException.NotFound(orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> retrieveMemberOrders(final Member member) {
        final List<Order> memberOrder = this.orderRepository.findByMember(member);
        return memberOrder.stream()
                .sorted(Comparator.comparing(Order::getOrderTime).reversed())
                .collect(Collectors.toList());
    }
}
