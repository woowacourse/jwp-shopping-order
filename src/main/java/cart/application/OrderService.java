package cart.application;

import java.time.LocalDateTime;
import java.util.List;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.orderitem.OrderItem;
import cart.dto.AuthMember;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao,
                        final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long order(AuthMember authMember, OrderRequest orderRequest) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        CartItems allCartItems = new CartItems(cartItemDao.selectAllByMemberId(findMember.getId()));
        List<OrderCartItemDto> orderCartItemDtos = orderRequest.getOrderCartItemDtos();
        CartItems cartItemsToOrder = new CartItems(allCartItems.getCartItemsByOrderCartItemDtos(orderCartItemDtos));
        int totalPrice = cartItemsToOrder.getTotalPrice();
        findMember.checkPayable(totalPrice);
        memberDao.updateMemberCash(findMember.pay(totalPrice));
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
        return insertedOrder.getId();
    }
}
