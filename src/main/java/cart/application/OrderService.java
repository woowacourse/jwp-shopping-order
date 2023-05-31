package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.request.OrderItemDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailsDto;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.dto.response.ProductResponse;
import cart.exception.CartItemException.IllegalMember;
import cart.exception.CartItemException.UnknownCartItem;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;

    public OrderService(final CartItemDao cartItemDao, final OrderRepository orderRepository) {
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
    }

    public long saveOrder(final Member member, final OrderRequest orderRequest) {
        List<CartItem> cartItems = findCartItems(orderRequest);

        checkIllegalMember(member, cartItems);
        checkUnknownCartItemIds(extractOrderCartItemIds(orderRequest), cartItems);

        Order order = Order.of(member, 3000, OrderItem.of(cartItems), 30000);
        order.checkPrice(orderRequest.getTotalPrice());

        long orderId = orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            cartItemDao.deleteById(cartItem.getId());
        }
        return orderId;
    }

    private void checkIllegalMember(final Member member, final List<CartItem> cartItems) {
        boolean isIllegalMember = List.copyOf(cartItems).stream().anyMatch(item -> !item.checkMember(member));
        if(isIllegalMember){
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

    // todo: 전체적으로 사용자 검증 필요

    public OrderResponse getOrderByOrderId(final long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        List<OrderDetailsDto> orderDetails = order.getOrderItems()
                .stream()
                .map(item -> new OrderDetailsDto(item.getQuantity(), ProductResponse.of(item.getProduct()))).
                collect(Collectors.toUnmodifiableList());
        return new OrderResponse(orderId, order.getCreatedAt(), order.getTotalPrice()+order.getShippingFee(), orderDetails);
    }

    public OrdersResponse getOrdersByMember(final Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders){
            List<OrderDetailsDto> orderDetails = order.getOrderItems()
                    .stream()
                    .map(item -> new OrderDetailsDto(item.getQuantity(), ProductResponse.of(item.getProduct()))).
                    collect(Collectors.toUnmodifiableList());
            orderResponses.add(new OrderResponse(order.getId(), order.getCreatedAt(), order.getTotalPrice()+order.getShippingFee(), orderDetails));
        }
        return new OrdersResponse(orderResponses);
    }


}
