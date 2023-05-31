package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
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
    private final ProductDao productDao;

    public OrderService(
            final CartItemDao cartItemDao,
            final OrderDao orderDao,
            final OrderItemDao orderItemDao,
            final MemberDao memberDao,
            final ProductDao productDao
    ) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
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
                        cartItem.getProduct().getId(),
                        orderId,
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
        final List<OrderItem> orderItems = orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItem(
                        orderItemEntity.getId(),
                        orderItemEntity.getQuantity(),
                        productDao.findById(orderItemEntity.getProductId()),
                        member
                ))
                .collect(Collectors.toUnmodifiableList());

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
}
