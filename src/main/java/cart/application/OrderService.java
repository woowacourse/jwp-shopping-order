package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderInfoDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderInfo;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderInfoDao orderInfoDao;

    public OrderService(MemberDao memberDao, CartItemDao cartItemDao, OrderDao orderDao, OrderInfoDao orderInfoDao) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderInfoDao = orderInfoDao;
    }

    public Long orderItems(Long memberId, OrderRequest orderRequest) {
        Order order = new Order(memberDao.getMemberById(memberId),
                cartItemDao.findByMemberId(memberId),
                orderRequest.getOriginalPrice(),
                orderRequest.getUsedPoint(),
                orderRequest.getPointToAdd());

        Long orderId = orderDao.save(order);

        for (CartItem cartItem : order.getCartItems()) {
            OrderInfo orderInfo = new OrderInfo(orderId, cartItem.getProduct(), cartItem.getQuantity());
            orderInfoDao.save(orderInfo);
        }

        memberDao.updateMember(order.getMember());

        cartItemDao.deleteByMemberId(memberId);

        return orderId;
    }

    public List<OrderResponse> findOrdersByMember(Member member) {
        List<Order> orders = orderDao.findByMemberId(member.getId());
        return orders.stream()
                .map(order -> OrderResponse.of(order, orderInfoDao.getOrderInfoByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    public OrderResponse findOrderDetail(Long orderId) {
        return OrderResponse.of(orderDao.findById(orderId), orderInfoDao.getOrderInfoByOrderId(orderId));
    }
}
