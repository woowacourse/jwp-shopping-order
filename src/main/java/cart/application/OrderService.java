package cart.application;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Product;
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
    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final OrderItemDao orderItemDao;
    private final DiscountPolicy discountPolicy;

    public OrderService(final MemberDao memberDao, final OrderDao orderDao, final ProductDao productDao,
        final OrderItemDao orderItemDao, final DiscountPolicy discountPolicy) {
        this.memberDao = memberDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderItemDao = orderItemDao;
        this.discountPolicy = discountPolicy;
    }

    public OrderResponse createOrder(final Long memberId, final OrderRequest orderRequest) {
        final Member member = findExistMemberById(memberId);
        final Order order = Order.beforePersisted(member, generateOrderItems(orderRequest));
        final Order persistOrder = orderDao.insert(order, discountPolicy.calculate(order.getTotalPrice()));
        saveOrderItems(persistOrder);
        final List<OrderItemResponse> orderProducts = OrderItemResponse.of(persistOrder.getOrderItems());
        //TODO : 주문한 상품 cartItem에서 제외 추가
        return new OrderResponse(persistOrder.getId(), orderProducts,
            persistOrder.getTotalPrice(), discountPolicy.calculate(persistOrder.getTotalPrice()));
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

    private void saveOrderItems(final Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemDao.insert(order.getId(), OrderItem.notPersisted(orderItem.getProduct(), orderItem.getQuantity()));
        }
    }

    public OrderResponse getOrderById(final Long memberId, final Long orderId) {
        final Member member = findExistMemberById(memberId);
        final List<OrderDto> orderDtos = orderDao.findByOrderId(orderId);
        final Order order = makeOrder(orderId, member, orderDtos);
        final Long totalPrice = order.getTotalPrice();
        return new OrderResponse(orderId, OrderItemResponse.of(order.getOrderItems()), totalPrice,
            discountPolicy.calculate(totalPrice));
    }

    private Order makeOrder(final Long orderId, final Member member, final List<OrderDto> orderDtos) {
        final List<OrderItem> orderItems = orderDtos.stream()
            .map(OrderDto::getOrderItem)
            .collect(Collectors.toList());
        return Order.persisted(orderId, member, new OrderItems(orderItems));
    }

    public OrdersResponse getOrderByMemberId(final Long memberId) {
        final Member member = findExistMemberById(memberId);
        final Map<Long, List<OrderDto>> memberOrderDtos = orderDao.findAllByMemberId(memberId).stream()
            .collect(Collectors.groupingBy(OrderDto::getOrderId));

        final List<OrderResponse> orderResponses = memberOrderDtos.entrySet().stream()
            .map((entry) -> {
                final Order order = makeOrder(entry.getKey(), member, entry.getValue());
                final Long totalPrice = order.getTotalPrice();
                return new OrderResponse(order.getId(), OrderItemResponse.of(order.getOrderItems()),
                    totalPrice, discountPolicy.calculate(totalPrice));
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
