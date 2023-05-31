package cart.application;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.OrderRepository;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.OrderInfo;
import cart.dto.OrderResponse;
import cart.dto.OrderRequest;
import cart.dto.SpecificOrderResponse;
import cart.exception.PointExceedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

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
        List<CartItem> cartItems = makeCartItemsFromId(request);
        Order order = new Order(null, cartItems, request.getOriginalPrice(),
                request.getUsedPoint(), request.getPointToAdd());
        orderRepository.insert(order);
        subtractPoint(member, request.getUsedPoint());
    }

    private List<CartItem> makeCartItemsFromId(OrderRequest request) {
        return request.getOrder().stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
    }

    private void subtractPoint(Member member, int usedPoint) {
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
                .map(order -> new OrderResponse(order.getId(), mapCartItemsToOrderInfo(order.getCartItems())))
                .collect(Collectors.toList());
    }

    private List<OrderInfo> mapCartItemsToOrderInfo(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::makeOrderInfo)
                .collect(Collectors.toList());
    }

    private OrderInfo makeOrderInfo(CartItem cartItem) {
        Product product = cartItem.getProduct();
        return new OrderInfo(product.getId(), product.getPrice(), product.getName(),
                product.getImageUrl(),cartItem.getQuantity());
    }

    public SpecificOrderResponse getSpecificOrder(Member member) {
        Order order = orderRepository.findById(member);
        return new SpecificOrderResponse(order.getId(), mapCartItemsToOrderInfo(order.getCartItems()),
                order.getOriginalPrice(), order.getUsedPoint(), order.getPointToAdd());
    }
}
