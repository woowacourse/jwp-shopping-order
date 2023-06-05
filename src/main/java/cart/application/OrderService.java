package cart.application;

import cart.Repository.OrderRepository;
import cart.domain.Cart;
import cart.domain.Member.Member;
import cart.domain.Order.Order;
import cart.domain.Point;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cart.Repository.mapper.OrderMapper.toOrderItemsFrom;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final PointService pointService;


    public OrderService(OrderRepository orderRepository, CartItemService cartItemService, PointService pointService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.pointService = pointService;
    }

    @Transactional
    public Long add(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        Cart cart = cartItemService.findByCartItemIds(orderRequest.getCartItemIds());
        cart.checkOwner(member);

        Order order = new Order(toOrderItemsFrom(cart));
        Point usePoint = new Point(orderRequest.getUsePoint());

        pointService.findPointByMember(member, usePoint, order.getTotalPrice());
        Long orderId = orderRepository.save(member, order);

        pointService.savePoint(usePoint, order.getTotalPrice(), orderId, member.getId());
        cartItemService.removeByIds(cart.getCartIds());
        return orderId;
    }

    public List<OrderResponse> findByMember(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }


    public OrderResponse findById(Long id, Member member) {
        Order order = orderRepository.findById(id);
        order.checkOwner(member);
        return OrderResponse.of(order);
    }
}
