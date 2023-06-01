package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderInfoDao;
import cart.domain.*;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Long order(Long memberId, OrderRequest orderRequest) {
        OrderManager orderManager = new OrderManager(orderRequest.getOrder()
                .stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toList()));

        Order order = new Order(memberDao.getMemberById(memberId),
                orderRequest.getOriginalPrice(),
                orderRequest.getUsedPoint(),
                orderRequest.getPointToAdd());

        orderManager.validateOrder(order);
        order.applyPoint();
        return persistOrderData(orderRequest, orderManager, order);
    }

    public Long persistOrderData(OrderRequest orderRequest, OrderManager orderManager, Order order) {
        Long orderId = orderDao.save(order);
        for (CartItem cartItem : orderManager.getCartItems()) {
            OrderInfo orderInfo = new OrderInfo(orderId, cartItem.getProduct(), cartItem.getQuantity());
            orderInfoDao.save(orderInfo);
        }

        for (Long cartItemId : orderRequest.getOrder()) {
            cartItemDao.deleteById(cartItemId);
        }
        memberDao.updateMember(order.getMember());

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
