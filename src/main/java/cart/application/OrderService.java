package cart.application;

import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.domain.Point;
import cart.domain.ShippingDiscountPolicy;
import cart.domain.ShippingFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemDao cartItemDao;

    public OrderService(OrderRepository orderRepository, CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.cartItemDao = cartItemDao;
    }

    public Long createOrder(Member member, OrderRequest orderRequest) {
        List<OrderItem> orderItemList = orderRequest.getOrderItemDtoList().stream()
                .map(orderItemDto -> new OrderItem(cartItemDao.findById(orderItemDto.getCartItemId()).getProduct(), orderItemDto.getQuantity()))
                .collect(toList());

        // TODO: optional 예외처리 구현하기
        ShippingFee shippingFee = orderRepository.findShippingFee();
        ShippingDiscountPolicy shippingDiscountPolicy = orderRepository.findShippingDiscountPolicy();

        Order newOrder = Order.of(member,
                shippingFee.getFee(),
                orderItemList,
                shippingDiscountPolicy.getThreshold(),
                new Point(orderRequest.getUsedPoint()));
        return orderRepository.saveOrder(member, newOrder);
    }

    public List<OrdersResponse> findAllOrdersByMember(Member member) {
        List<Order> orders = orderRepository.findAllOrdersByMember(member);
        return orders.stream()
                .map(OrdersResponse::from)
                .collect(toList());
    }

    public OrderResponse findOrderDetailsByOrderId(Member member, Long orderId) {
        Order order = orderRepository.findOrderById(member, orderId);
        return OrderResponse.from(order);
    }

}
