package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.delivery.DeliveryPolicy;
import cart.domain.discount.DiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.dto.OrderDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.exception.MemberNotExistException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final OrderItemDao orderItemDao;
    private final DiscountPolicy discountPolicy;
    private final DeliveryPolicy deliveryPolicy;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao, final OrderDao orderDao,
        final ProductDao productDao,
        final OrderItemDao orderItemDao, final DiscountPolicy discountPolicy, final DeliveryPolicy deliveryPolicy) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderItemDao = orderItemDao;
        this.discountPolicy = discountPolicy;
        this.deliveryPolicy = deliveryPolicy;
    }

    public OrderResponse createOrder(final Long memberId, final OrderRequest orderRequest) {
        final Member member = findExistMemberById(memberId);
        final Order order = Order.beforePersisted(member, generateOrderItems(orderRequest),
            orderRequest.getOrderTime());
        final Long productPrice = order.getProductPrice();
        final Order persistOrder = orderDao.insert(order, discountPolicy.calculate(productPrice),
            deliveryPolicy.getDeliveryFee(productPrice));

        saveOrderItems(persistOrder);
        deleteCartItems(persistOrder);

        return generateOrderResponseAfterCreate(persistOrder);
    }

    private void saveOrderItems(final Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemDao.insert(order.getId(), OrderItem.notPersisted(orderItem.getProduct(), orderItem.getQuantity()));
        }
    }

    private void deleteCartItems(final Order order) {
        final List<Long> productIds = order.getOrderItems()
            .stream()
            .map((orderItem -> orderItem.getProduct().getId()))
            .collect(Collectors.toList());
        cartItemDao.deleteByMemberIdAndProductIds(order.getMemberId(), productIds);
    }

    private OrderResponse generateOrderResponseAfterCreate(final Order order) {
        final Long productPrice = order.getProductPrice();
        final Long discountPrice = discountPolicy.calculate(productPrice);
        final Long deliveryFee = deliveryPolicy.getDeliveryFee(productPrice);

        return new OrderResponse(
            order.getId(),
            OrderItemResponse.of(order.getOrderItems()),
            productPrice,
            discountPrice,
            deliveryFee,
            calculateTotalPrice(productPrice, discountPrice, deliveryFee)
        );
    }

    private Long calculateTotalPrice(final Long productPrice, final Long discountPrice, final Long deliveryFee) {
        return productPrice - discountPrice + deliveryFee;
    }

    private OrderItems generateOrderItems(final OrderRequest orderRequest) {
        final List<OrderItem> orderItems = orderRequest.getOrderItems()
            .stream()
            .map((orderItem) -> {
                final Product pesistedProduct = productDao.getProductById(orderItem.getId());
                return OrderItem.notPersisted(pesistedProduct, orderItem.getQuantity());
            })
            .collect(Collectors.toList());

        return new OrderItems(orderItems);
    }

    private Order makeOrder(final Member member, final List<OrderDto> orderDtos) {
        final Long orderId = orderDtos.get(0).getOrderId();
        final LocalDateTime orderTime = orderDtos.get(0).getOrderTime();
        final List<OrderItem> orderItems = orderDtos.stream()
            .map(OrderDto::getOrderItem)
            .collect(Collectors.toList());
        return Order.persisted(orderId, member, new OrderItems(orderItems), orderTime);
    }

    public OrderResponse getOrderById(final Long memberId, final Long orderId) {
        final Member member = findExistMemberById(memberId);
        final List<OrderDto> orderDtos = orderDao.findByOrderId(orderId);
        final Order order = makeOrder(member, orderDtos);

        return generateOrderResponseAfterFind(order, orderDtos.get(0));
    }

    private OrderResponse generateOrderResponseAfterFind(final Order order, final OrderDto orderDto) {
        final Long productPrice = orderDto.getOrderProductPrice();
        final Long discountPrice = orderDto.getOrderDiscountPrice();
        final Long deliveryFee = orderDto.getOrderDeliveryFee();

        return new OrderResponse(
            order.getId(),
            OrderItemResponse.of(order.getOrderItems()), productPrice,
            discountPrice,
            deliveryFee,
            calculateTotalPrice(productPrice, discountPrice, deliveryFee)
        );
    }

    public OrdersResponse getOrderByMemberId(final Long memberId) {
        final Member member = findExistMemberById(memberId);
        final Map<Long, List<OrderDto>> memberOrderDtos = orderDao.findAllByMemberId(memberId).stream()
            .collect(Collectors.groupingBy(OrderDto::getOrderId));

        final List<OrderResponse> orderResponses = memberOrderDtos.values().stream()
            .map(orderDtos -> {
                final Order order = makeOrder(member, orderDtos);
                return generateOrderResponseAfterCreate(order);
            })
            .collect(Collectors.toList());

        return new OrdersResponse(orderResponses);
    }

    private Member findExistMemberById(final Long id) {
        final Member member = memberDao.getMemberById(id);
        if (Objects.isNull(member)) {
            throw new MemberNotExistException("멤버가 존재하지 않습니다.");
        }
        return member;
    }
}
