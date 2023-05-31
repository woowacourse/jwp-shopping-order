package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.CreditCard;
import cart.domain.Member;
import cart.domain.Point;
import cart.dto.OrderCreateRequest;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
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
        member.addPoint(Point.fromPayment(totalPrice));
        memberDao.update(member);

        final Long orderId = orderDao.create(OrderEntity.toCreate(member.getId(), usingPoint));
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
                .mapToInt(e -> e.getProduct().getPrice())
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
}
