package cart.application;

import cart.dao.CartItemDao;
import cart.dao.PointDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.PointPolicy;
import cart.domain.ShippingPolicy;
import cart.dto.request.OrderItemDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderAdditionResponse;
import cart.dto.response.OrderDetailsDto;
import cart.dto.response.OrderResponse;
import cart.dto.response.ProductResponse;
import cart.exception.CartItemException.IllegalMember;
import cart.exception.CartItemException.InvalidCartItem;
import cart.exception.CartItemException.QuantityNotSame;
import cart.exception.CartItemException.UnknownCartItem;
import cart.exception.OrderException.LackOfPoint;
import cart.repository.OrderRepository;
import cart.repository.ShippingPolicyRepository;
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
    private final ShippingPolicyRepository shippingPolicyRepository;
    private final PointDao pointDao;

    public OrderService(
            final CartItemDao cartItemDao,
            final OrderRepository orderRepository,
            final ShippingPolicyRepository shippingPolicyRepository,
            final PointDao pointDao
    ) {
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
        this.shippingPolicyRepository = shippingPolicyRepository;
        this.pointDao = pointDao;
    }

    public OrderAdditionResponse saveOrder(final Member member, final OrderRequest orderRequest) {
        List<CartItem> cartItems = findCartItems(orderRequest);

        checkIllegalMember(member, cartItems);
        checkUnknownCartItemIds(extractOrderCartItemIds(orderRequest), cartItems);
        checkQuantity(orderRequest.getOrder(), cartItems);

        ShippingPolicy shippingPolicy = shippingPolicyRepository.findShippingPolicy();
        List<OrderItem> orderItems = OrderItem.of(cartItems);
        Order order = Order.of(member, shippingPolicy.calculateShippingFee(orderItems), orderRequest.getUsedPoint(), orderItems);
        order.checkTotalProductsPrice(orderRequest.getTotalProductsPrice());
        order.checkShippingFee(orderRequest.getShippingFee());

        long totalPoint = pointDao.selectByMemberId(member.getId());
        long usedPoint = orderRequest.getUsedPoint();
        if (totalPoint < usedPoint) {
            throw new LackOfPoint();
        }
        long newEarnedPoint = PointPolicy.getEarnedPoint(order.getPayment());

        long orderId = orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            cartItemDao.deleteById(cartItem.getId());
        }
        pointDao.update(member.getId(), totalPoint - usedPoint + newEarnedPoint);

        return new OrderAdditionResponse(orderId, newEarnedPoint);
    }

    private void checkQuantity(final List<OrderItemDto> orders, final List<CartItem> cartItems) {
        for (OrderItemDto orderItemDto : orders) {
            CartItem cartItem = cartItems.stream()
                    .filter(item -> Objects.equals(item.getId(), orderItemDto.getCartItemId())).
                    findFirst()
                    .orElseThrow(InvalidCartItem::new);
            if (orderItemDto.getQuantity() != cartItem.getQuantity()) {
                throw new QuantityNotSame();
            }
        }
    }

    private void checkIllegalMember(final Member member, final List<CartItem> cartItems) {
        boolean isIllegalMember = List.copyOf(cartItems).stream().anyMatch(item -> !item.checkMember(member));
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
                .map(OrderItemDto::getCartItemId).collect(Collectors.toList());
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

    public OrderResponse getOrderByOrderId(final Member member, final long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.checkOwner(member);
        List<OrderDetailsDto> orderDetails = order.getOrderItems()
                .stream()
                .map(item -> new OrderDetailsDto(item.getQuantity(), ProductResponse.of(item.getProduct()))).
                collect(Collectors.toUnmodifiableList());
        return new OrderResponse(orderId, order.getCreatedAt(), order.getTotalProductsPrice(), order.getShippingFee(), order.getUsedPoint(), orderDetails);
    }

    public List<OrderResponse> getOrdersByMember(final Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderDetailsDto> orderDetails = order.getOrderItems()
                    .stream()
                    .map(item -> new OrderDetailsDto(item.getQuantity(), ProductResponse.of(item.getProduct()))).
                    collect(Collectors.toUnmodifiableList());
            orderResponses.add(new OrderResponse(order.getId(), order.getCreatedAt(), order.getTotalProductsPrice(), order.getShippingFee(), order.getUsedPoint(), orderDetails));
        }
        return orderResponses;
    }


}
