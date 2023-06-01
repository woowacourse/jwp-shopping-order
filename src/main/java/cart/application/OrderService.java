package cart.application;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderRepository;
import cart.dto.OrderListResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;
    private final MemberDao memberDao;

    public OrderService(final CartItemService cartItemService, final OrderRepository orderRepository, final MemberDao memberDao) {
        this.cartItemService = cartItemService;
        this.orderRepository = orderRepository;
        this.memberDao = memberDao;
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final int usedPoints = orderRequest.getUsedPoints();
        final List<CartItem> orderedCartItems = cartItemService.findSelectedCartItems(member, orderRequest.getCartItemIds());

        final Order order = Order.of(member, usedPoints, orderedCartItems);

        final Long orderId = orderRepository.saveOrder(order);
        orderedCartItems.forEach(item -> cartItemService.remove(member, item.getId()));
        memberDao.updateMemberPoint(calculatePoints(member, order));

        return orderId;
    }

    private Member calculatePoints(final Member member, final Order order) {
        return member.spendPoint(order.getUsedPoints())
                .chargePoint(order.getEarnedPoints());
    }

    public OrderResponse findById(final Member member, final Long id) {
        final Order order = orderRepository.findById(id);
        return OrderResponse.of(order);
    }

    public OrderListResponse findPageByIndex(final Member member, final Long idx) {
        final List<Order> orders = orderRepository.findPageByIndex(member.getId(), idx);
        return OrderListResponse.of(orders);
    }
}
