package cart.service;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.domain.PointPolicy;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.OrderInfo;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.SpecificOrderResponse;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final PointPolicy pointPolicy;

    public OrderService(final OrderRepository orderRepository, final MemberRepository memberRepository, final CartItemDao cartItemDao, final ProductDao productDao, final PointPolicy pointPolicy) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.pointPolicy = pointPolicy;
    }

    public Long addOrder(final Member member, final OrderRequest orderRequest) {
        final List<OrderInfo> orderInfos = orderRequest.getOrderInfos();

        // 장바구니에 해당 상품이 있는지 조회
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());

        final List<OrderItem> orderItems = orderInfos.stream()
                .map(orderInfo -> {
                    final Product product = productDao.getProductById(orderInfo.getProductId());

                    final CartItem cartItem = cartItems.stream()
                            .filter(it -> it.hasSameProduct(product))
                            .findAny()
                            .orElseThrow(() -> new IllegalArgumentException("장바구니에 존재하지 않는 상품입니다."));

                    if (!cartItem.isSameQuantity(orderInfo.getQuantity())) {
                        throw new IllegalArgumentException("장바구니와 수량이 다릅니다.");
                    }

                    return new OrderItem(product, orderInfo.getQuantity());
                }).collect(Collectors.toUnmodifiableList());

        final Order order = Order.from(member, orderRequest.getPayment(), orderRequest.getPoint(), orderItems);

        // 포인트 적립
        final Point savePoint = pointPolicy.save(order.getPayment());
        final Member newMember = order.calculateMemberPoint(savePoint);
        memberRepository.updateMemberPoint(newMember);

        return orderRepository.addOrder(order);
    }

    public List<OrderResponse> getAllOrders(final Member member) {
        final List<Order> orders = orderRepository.getAllOrders(member);
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public SpecificOrderResponse getOrderById(final Member member, final Long orderId) {
        final Order order = orderRepository.getOrderById(member, orderId);
        return SpecificOrderResponse.from(order);
    }
}
