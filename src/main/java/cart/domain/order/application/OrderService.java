package cart.domain.order.application;

import java.time.LocalDateTime;
import java.util.List;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.cartitem.domain.CartItems;
import cart.domain.cartitem.persistence.CartItemDao;
import cart.domain.member.domain.Member;
import cart.domain.member.persistence.MemberDao;
import cart.domain.order.application.dto.OrderRequest;
import cart.domain.order.application.dto.OrderResponse;
import cart.domain.order.application.dto.OrdersResponse;
import cart.domain.order.domain.Order;
import cart.domain.order.domain.OrderItem;
import cart.domain.order.domain.OrderItems;
import cart.domain.order.persistence.OrderDao;
import cart.domain.order.persistence.OrderItemDao;
import cart.domain.order.validator.OrderValidator;
import cart.global.config.AuthMember;
import cart.global.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderValidator orderValidator;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao,
                        final OrderDao orderDao, final OrderItemDao orderItemDao,
                        final OrderValidator orderValidator) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderValidator = orderValidator;
    }

    public Long order(AuthMember authMember, OrderRequest orderRequest) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        CartItems cartItemsToOrder = findCartItemsToOrder(orderRequest, findMember);
        orderValidator.validate(cartItemsToOrder, orderRequest.getOrderCartItemDtos());
        int totalPrice = cartItemsToOrder.getTotalPrice();
        findMember.checkPayable(totalPrice);
        memberDao.updateMemberCash(findMember.pay(totalPrice));
        Order insertedOrder = saveOrder(findMember, cartItemsToOrder, totalPrice);
        return insertedOrder.getId();
    }

    private Order saveOrder(Member findMember, CartItems cartItemsToOrder, int totalPrice) {
        for (Long cartItemIdToOrder : cartItemsToOrder.getCartItemIds()) {
            cartItemDao.deleteById(cartItemIdToOrder);
        }
        Order insertedOrder = orderDao.insert(new Order(findMember, totalPrice, LocalDateTime.now()));
        List<CartItem> rawCartItemsOrder = cartItemsToOrder.getCartItems();
        for (CartItem cartItem : rawCartItemsOrder) {
            OrderItem orderItem = new OrderItem(insertedOrder, cartItem.getId(), cartItem.getProductName(),
                    cartItem.getProductPrice(), cartItem.getProductImageUrl(), cartItem.getQuantity());
            orderItemDao.insert(orderItem);
        }
        return insertedOrder;
    }

    private CartItems findCartItemsToOrder(OrderRequest orderRequest, Member findMember) {
        CartItems allCartItems = new CartItems(cartItemDao.selectAllByMemberId(findMember.getId()));
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        return allCartItems.getCartItemsByCartItemIds(cartItemIds);
    }

    public OrdersResponse findOrders(AuthMember authMember) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        OrderItems orderItems = new OrderItems(orderItemDao.selectAllByMemberId(findMember.getId()));
        List<OrderResponse> orderResponses = orderItems.toOrderResponses();
        return new OrdersResponse(orderResponses);
    }

    public OrderResponse findOrder(AuthMember authMember, Long orderId) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        checkOrderExist(orderId);
        OrderItems orderItems = new OrderItems(orderItemDao.selectAllByMemberIdAndOrderId(findMember.getId(), orderId));
        return orderItems.getOrderResponse(orderId);
    }

    private void checkOrderExist(Long orderId) {
        if (orderDao.isNotExistById(orderId)) {
            throw new OrderNotFoundException("주문 ID에 해당하는 주문을 찾을 수 없습니다.");
        }
    }
}
