package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.request.OrderItemDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailsDto;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.dto.response.ProductResponse;
import cart.exception.CartItemException.QuantityNotSame;
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

    // todo: 전체적으로 사용자 검증 필요

    public long saveOrder(final Member member, final OrderRequest orderRequest) {
        List<CartItem> cartItems = new ArrayList<>();
        List<Long> unknownItemIds = new ArrayList<>();
        List<Long> strangeItemIds = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderRequest.getOrder()) {
            CartItem cartItem = cartItemDao.findById(orderItemDto.getCartItemId());
            //todo: 등록되지 않은 상품인가? 아니면 장바구니에 없는 상품인가? 구분 가능? 언제 반영?
            // 지금은 innerJoin이라 빼고 가져오는 듯?
            if (cartItem == null) {
                unknownItemIds.add(orderItemDto.getCartItemId());
                continue;
            }
            cartItems.add(cartItem);
            if (orderItemDto.getQuantity() != cartItem.getQuantity()) {
                strangeItemIds.add(orderItemDto.getCartItemId());
            }
        }
        if (strangeItemIds.size() != 0) {
            throw new QuantityNotSame(strangeItemIds);
        }
        if (unknownItemIds.size() != 0) {
            throw new UnknownCartItem(unknownItemIds);
        }

        Order order = Order.of(member, 3000, OrderItem.of(cartItems), 30000);
        order.checkPrice(orderRequest.getTotalPrice());
        // order.checkMember(member); // 멤버가 아닌데 주문을 등록하는 경우

        long orderId = orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            cartItemDao.deleteById(cartItem.getId());
        }
        return orderId;
    }

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
