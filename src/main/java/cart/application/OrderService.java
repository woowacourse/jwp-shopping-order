package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.dto.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;

    private final CartItemDao cartItemDao;
    private final CartItemService cartItemService;
    private final ProductDao productDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao, CartItemService cartItemService, ProductDao productDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.cartItemService = cartItemService;
        this.productDao = productDao;
    }

    public OrderResponse add(Member member, OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(orderItemRequest -> {
                    Product product = productDao.getProductById(orderItemRequest.getProductId());
                    return new OrderItem(product, orderItemRequest.getQuantity());
                })
                .collect(Collectors.toList());
        return orderDao.save(new Order(member, cartItems));
    }
}
