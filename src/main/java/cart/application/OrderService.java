package cart.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderDto;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;

@Service
public class OrderService {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final OrderItemDao orderItemDao;
    private final OrderDao orderDao;

    public OrderService(CartService cartService, CartItemService cartItemService, OrderItemDao orderItemDao,
            OrderDao orderDao) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
    }

    public void order(Member member, List<OrderRequest> requests) {
        Cart cart = cartService.getCartOf(member);
        List<CartItem> itemsToOrder = new ArrayList<>();
        for (OrderRequest request : requests) {
            CartItem item = cartItemService.getItemBy(request.getCartItemId());
            itemsToOrder.add(item);
        }

        Order order = cart.order(itemsToOrder);

        save(order);
        cartService.save(cart);
    }

    private void save(Order order) {
        Long orderId = orderDao.insert(OrderDto.of(order));
        orderItemDao.insertAll(orderId, order.getOrderItems());
    }
}
