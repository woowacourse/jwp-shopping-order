package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
    }

    public Long add(Member member, OrderRequest orderRequest) {
        List<CartItem> cartItems = orderRequest.getCartIds().stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toList());
        return orderDao.save(new Order(member, cartItems));
    }
}
