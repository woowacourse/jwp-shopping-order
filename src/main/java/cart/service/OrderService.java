package cart.service;

import cart.controller.dto.request.OrderRequest;
import cart.controller.dto.response.OrderResponse;
import cart.controller.dto.response.OrderThumbnailResponse;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderInfo;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.OrderProducts;
import cart.domain.Product;
import cart.domain.discount_strategy.DiscountCalculator;
import cart.exception.NotOwnerException;
import cart.exception.PaymentAmountNotEqualException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final DiscountCalculator discountCalculator;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository,
                        final DiscountCalculator discountCalculator) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.discountCalculator = discountCalculator;
    }

    @Transactional
    public long save(final Member member, final OrderRequest request) {
        final List<CartItem> cartItems = cartItemRepository.findByIds(request.getCartItems());
        validateCartItemsOwner(member, cartItems);
        final Order order = createOrder(member, cartItems);
        validatePaymentAmount(order, request.getPaymentAmount());
        final long orderId = orderRepository.save(order);
        cartItemRepository.deleteAll(request.getCartItems());
        return orderId;
    }

    private void validateCartItemsOwner(final Member member, final List<CartItem> cartItems) {
        final boolean isIdEquals = cartItems.stream()
                .map(CartItem::getMemberId)
                .allMatch(member::isIdEquals);
        if (isIdEquals) {
            return;
        }
        throw new NotOwnerException();
    }

    private Order createOrder(final Member member, final List<CartItem> cartItems) {
        return new Order(
                member.getId(),
                new OrderItems(convertCartItemsToOrderItems(cartItems), discountCalculator)
        );
    }

    private List<OrderItem> convertCartItemsToOrderItems(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toUnmodifiableList());
    }

    private void validatePaymentAmount(final Order order, final int requestPayment) {
        if (order.isPaymentAmountEqual(requestPayment)) {
            return;
        }
        throw new PaymentAmountNotEqualException();
    }

    public List<OrderThumbnailResponse> findByMember(final Member member) {
        final List<OrderInfo> orderInfos = orderRepository.findByMember(member);
        if (orderInfos.isEmpty()) {
            return Collections.emptyList();
        }
        final Map<Long, List<Product>> orderProductsInOrderId = orderRepository.findProductsByIds(getIdsInOrderInfos(orderInfos));
        final OrderProducts orderProducts = OrderProducts.of(orderInfos, orderProductsInOrderId);
        return orderProducts.getOrderProducts().stream()
                .map(OrderThumbnailResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Long> getIdsInOrderInfos(final List<OrderInfo> orderInfos) {
        return orderInfos.stream()
                .map(OrderInfo::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderResponse findById(final Member member, final long id) {
        final Order order = orderRepository.findById(id);
        validateOrderOwner(member, order);
        return OrderResponse.from(order);
    }

    private void validateOrderOwner(final Member member, final Order order) {
        if (member.isIdEquals(order.getMemberId())) {
            return;
        }
        throw new NotOwnerException();
    }
}
