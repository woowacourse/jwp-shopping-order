package cart.application;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.CreditCard;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import cart.entity.MemberEntity;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MemberDao memberDao,
            final CartItemRepository cartItemRepository,
            final OrderRepository orderRepository
    ) {
        this.memberDao = memberDao;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long saveOrder(final Member member, final OrderCreateRequest orderCreateRequest) {
        final List<Long> cartItemIds = orderCreateRequest.getCartItemIds();
        final List<CartItem> cartItemsToOrder = cartItemRepository.findByIds(cartItemIds);
        validateOwner(member, cartItemsToOrder);

        final Point usingPoint = new Point(orderCreateRequest.getPoint());
        final CreditCard creditCard = new CreditCard(orderCreateRequest.getCardNumber(), orderCreateRequest.getCvc());

        member.usePoint(usingPoint);
        final int totalPrice = calculateTotalPrice(cartItemsToOrder);
        creditCard.payWithPoint(totalPrice, usingPoint);
        final Point savingPoint = Point.fromPayment(totalPrice);
        member.addPoint(savingPoint);
        memberDao.update(MemberEntity.from(member));

        final List<OrderItem> orderItems = convertToOrderItems(cartItemsToOrder);

        final Order order = new Order(orderItems, usingPoint, savingPoint);
        final Long orderId = orderRepository.save(order);

        cartItemIds.forEach(cartItemRepository::deleteById);

        return orderId;
    }

    private void validateOwner(final Member member, final List<CartItem> cartItemsToOrder) {
        cartItemsToOrder.forEach(cartItem -> cartItem.checkOwner(member));
    }

    private int calculateTotalPrice(final List<CartItem> cartItemsToOrder) {
        return cartItemsToOrder.stream()
                .mapToInt(e -> e.getProduct().getPrice() * e.getQuantity())
                .sum();
    }

    private List<OrderItem> convertToOrderItems(final List<CartItem> cartItemsToOrder) {
        return cartItemsToOrder.stream()
                .map(cartItem -> new OrderItem(
                        cartItem.getId(),
                        cartItem.getQuantity(),
                        cartItem.getProduct(),
                        cartItem.getMember()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderDetailResponse findOrderDetailById(Member member, final Long id) {
        final Order order = orderRepository.findById(id, member);
        return OrderDetailResponse.from(order);
    }

    public List<OrderDetailResponse> findOrdersByMember(final Member member) {
        final List<Order> orders = orderRepository.findByMember(member);

        return orders.stream()
                .map(OrderDetailResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
