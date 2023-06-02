package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.domain.PointPolicy;
import cart.domain.ShippingPolicy;
import cart.dto.request.OrderItemDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderAdditionResponse;
import cart.dto.response.OrderResponse;
import cart.exception.CartItemException.IllegalMember;
import cart.exception.CartItemException.InvalidCartItem;
import cart.exception.CartItemException.QuantityNotSame;
import cart.exception.CartItemException.UnknownCartItem;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;
    private final OrderPolicyService orderPolicyService;
    private final PointService pointService;

    public OrderService(
            final CartItemDao cartItemDao,
            final OrderRepository orderRepository,
            final OrderPolicyService orderPolicyService,
            final PointService pointService
    ) {
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
        this.orderPolicyService = orderPolicyService;
        this.pointService = pointService;
    }

    public OrderAdditionResponse saveOrder(final Member member, final OrderRequest orderRequest) {
        List<CartItem> cartItems = findCartItems(orderRequest);
        Point usedPoint = new Point(orderRequest.getUsedPoint());

        checkIllegalMember(member, cartItems);
        checkUnknownCartItemIds(extractOrderCartItemIds(orderRequest), cartItems);
        checkQuantity(orderRequest.getOrder(), cartItems);

        ShippingPolicy shippingPolicy = orderPolicyService.findShippingPolicy();
        PointPolicy pointPolicy = orderPolicyService.findPointPolicy();

        List<OrderItem> orderItems = OrderItem.of(cartItems);
        Order order = Order.of(member, shippingPolicy.calculateShippingFee(orderItems), usedPoint.getPoint(), orderItems);
        order.checkTotalProductsPrice(orderRequest.getTotalProductsPrice());
        order.checkShippingFee(orderRequest.getShippingFee());

        Point totalPoint = new Point(pointService.findByMember(member));
        Point newEarnedPoint = pointPolicy.getEarnedPoint(order.getPayment());
        Point updatedPoint = totalPoint.use(usedPoint)
                .add(newEarnedPoint);

        long orderId = orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            cartItemDao.deleteById(cartItem.getId());
        }
        pointService.updateByMember(member, updatedPoint.getPoint());

        return new OrderAdditionResponse(orderId, newEarnedPoint.getPoint());
    }

    private void checkQuantity(final List<OrderItemDto> orders, final List<CartItem> cartItems) {
        for (OrderItemDto orderItemDto : orders) {
            CartItem cartItem = cartItems.stream()
                    .filter(item -> Objects.equals(item.getId(), orderItemDto.getCartItemId()))
                    .findFirst()
                    .orElseThrow(InvalidCartItem::new);
            if (orderItemDto.getQuantity() != cartItem.getQuantity()) {
                throw new QuantityNotSame();
            }
        }
    }

    private void checkIllegalMember(final Member member, final List<CartItem> cartItems) {
        boolean isIllegalMember = List.copyOf(cartItems)
                .stream()
                .anyMatch(item -> !item.checkMember(member));
        if (isIllegalMember) {
            throw new IllegalMember();
        }
    }

    private List<CartItem> findCartItems(final OrderRequest orderRequest) {
        List<CartItem> cartItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderRequest.getOrder()) {
            CartItem cartItem = cartItemDao.findById(orderItemDto.getCartItemId());
            if (cartItem == null) {
                continue;
            }
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    private static List<Long> extractOrderCartItemIds(final OrderRequest orderRequest) {
        return orderRequest.getOrder()
                .stream()
                .map(OrderItemDto::getCartItemId)
                .collect(Collectors.toList());
    }

    private void checkUnknownCartItemIds(final List<Long> orderCartItemIds, final List<CartItem> cartItems) {
        List<Long> cartItemIds = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        List<Long> unknownCartItemIds = orderCartItemIds.stream()
                .filter(id -> !cartItemIds.contains(id))
                .collect(Collectors.toList());
        if (unknownCartItemIds.size() != 0) {
            throw new UnknownCartItem(unknownCartItemIds);
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderId(final Member member, final long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.checkOwner(member);
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByMember(final Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

}
