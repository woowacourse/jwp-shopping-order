package cart.application;

import cart.application.domain.OrderInfo;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.OrderRepository;
import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.application.domain.Product;
import cart.presentation.dto.response.OrderDto;
import cart.presentation.dto.response.OrderResponse;
import cart.presentation.dto.request.OrderRequest;
import cart.presentation.dto.response.SpecificOrderResponse;
import cart.application.exception.PointExceedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    // TODO: MEMBER 검증로직 추가(service에서 할지, resolver에서 할지)

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                        MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public void issue(Member member, OrderRequest request) {
        Order order = new Order(null, member, makeOrderInfoFromRequest(request),
                request.getOriginalPrice(),
                request.getUsedPoint(),
                request.getPointToAdd());

        orderRepository.insert(order);
        subtractUserPoint(member, request.getUsedPoint());
    }

    private List<OrderInfo> makeOrderInfoFromRequest(OrderRequest request) {
        return makeCartItemsFromIds(request.getCartItemIds()).stream()
                .map(this::makeCartItemToOrderInfo)
                .collect(Collectors.toList());
    }

    private List<CartItem> makeCartItemsFromIds(List<Long> ids) {
        return ids.stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
    }

    private OrderInfo makeCartItemToOrderInfo(CartItem cartItem) {
        Product product = cartItem.getProduct();
        return new OrderInfo(null, product, product.getName(), product.getPrice(),
                product.getImageUrl(), cartItem.getQuantity());
    }

    private void subtractUserPoint(Member member, long usedPoint) {
        if (member.getPoint() - usedPoint < 0) {
            throw new PointExceedException();
        }
        Member updatedMember = new Member(member.getId(), member.getEmail(),
                member.getPassword(), member.getPoint() - usedPoint);
        memberRepository.update(updatedMember);
    }

    public List<OrderResponse> getAllOrders(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream()
                .map(order -> new OrderResponse(order.getId(), mapOrderInfoToOrderDto(order.getOrderInfo())))
                .collect(Collectors.toList());
    }

    private List<OrderDto> mapOrderInfoToOrderDto(List<OrderInfo> orderInfo) {
        return orderInfo.stream()
                .map(this::makeOrderDto)
                .collect(Collectors.toList());
    }

    private OrderDto makeOrderDto(OrderInfo orderInfo) {
        Product product = orderInfo.getProduct();
        return new OrderDto(product.getId(), product.getPrice(), product.getName(),
                product.getImageUrl(), orderInfo.getQuantity());
    }

    public SpecificOrderResponse getSpecificOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        return new SpecificOrderResponse(order.getId(), mapOrderInfoToOrderDto(order.getOrderInfo()),
                order.getOriginalPrice(), order.getUsedPoint(), order.getPointToAdd());
    }
}
