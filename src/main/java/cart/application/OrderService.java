package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.CreditCard;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemResponse;
import cart.dto.ProductResponse;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.IllegalMemberException;
import cart.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderService(
            final CartItemDao cartItemDao,
            final OrderDao orderDao,
            final OrderItemDao orderItemDao,
            final MemberDao memberDao
    ) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public Long createOrder(final Member member, final OrderCreateRequest orderCreateRequest) {
        final List<Long> cartItemIds = orderCreateRequest.getCartItemIds();
        final List<CartItem> cartItemsToOrder = findCartItemsById(cartItemIds);
        final int usingPoint = orderCreateRequest.getPoint();
        final CreditCard creditCard = new CreditCard(
                orderCreateRequest.getCardNumber(),
                orderCreateRequest.getCvc()
        );

        member.usePoint(usingPoint);
        final int totalPrice = calculateTotalPrice(cartItemsToOrder);
        creditCard.payWithPoint(totalPrice, usingPoint);
        final Point savingPoint = Point.fromPayment(totalPrice);
        member.addPoint(savingPoint);
        memberDao.update(member);

        final Long orderId = orderDao.create(OrderEntity.toCreate(member.getId(), usingPoint, savingPoint.getValue()));
        createOrderItems(cartItemsToOrder, orderId);
        cartItemIds.forEach(cartItemDao::deleteById);

        return orderId;
    }

    private List<CartItem> findCartItemsById(final List<Long> cartItemIds) {
        return cartItemIds
                .stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toUnmodifiableList());
    }

    private int calculateTotalPrice(final List<CartItem> cartItemsToOrder) {
        return cartItemsToOrder.stream()
                .mapToInt(e -> e.getProduct().getPrice() * e.getQuantity())
                .sum();
    }

    private void createOrderItems(final List<CartItem> cartItemsToOrder, final Long orderId) {
        final List<OrderItemEntity> orderItemEntities = cartItemsToOrder.stream()
                .map(cartItem -> OrderItemEntity.toCreate(
                        orderId, cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getImageUrl(),
                        cartItem.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
        orderItemDao.create(orderItemEntities);
    }

    public OrderDetailResponse findOrderDetailById(Member member, final Long id) {
        final OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 주문 정보가 없습니다."));
        validateOwner(member, orderEntity);

        final List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(id);
        final List<OrderItem> orderItems = OrderItemEntity.toDomain(orderItemEntities, member);

        final Order order = new Order(
                orderEntity.getId(),
                orderItems,
                orderEntity.getUsedPoint(),
                orderEntity.getSavedPoint(),
                orderEntity.getOrderedAt()
        );
        return new OrderDetailResponse(
                order.getId(),
                order.getUsedPoint().getValue(),
                order.getSavedPoint().getValue(),
                order.getOrderedAt(),
                order.getOrderItems().stream()
                        .map(orderItem -> new OrderItemResponse(
                                orderItem.getId(),
                                orderItem.getQuantity(),
                                ProductResponse.of(orderItem.getProduct())
                        ))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    private void validateOwner(final Member member, final OrderEntity orderEntity) {
        final Member orderedMember = memberDao.findById(orderEntity.getMemberId());
        if (member.equals(orderedMember)) {
            return;
        }
        throw new IllegalMemberException("주문자와 로그인한 사용자의 정보가 일치하지 않습니다.");
    }

    public List<OrderDetailResponse> findOrders(final Member member) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        orderEntities.forEach(orderEntity -> validateOwner(member, orderEntity));
        final List<Order> orders = orderEntities.stream()
                .map(orderEntity -> new Order(
                        orderEntity.getId(),
                        OrderItemEntity.toDomain(orderItemDao.findByOrderId(orderEntity.getId()), member),
                        orderEntity.getUsedPoint(),
                        orderEntity.getSavedPoint(),
                        orderEntity.getOrderedAt()
                )).collect(Collectors.toUnmodifiableList());
        return orders.stream()
                .map(order -> new OrderDetailResponse(
                        order.getId(),
                        order.getUsedPoint().getValue(),
                        order.getSavedPoint().getValue(),
                        order.getOrderedAt(),
                        OrderItemResponse.from(order.getOrderItems())
                ))
                .collect(Collectors.toUnmodifiableList());
    }
}
