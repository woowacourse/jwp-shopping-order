package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.dto.OrderDto;
import cart.dao.dto.OrderProductDto;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.domain.OrderProduct;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final CartItemDao cartItemDao;

    public OrderRepository(final OrderDao orderDao,
                           final OrderProductDao orderProductDao,
                           final CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.cartItemDao = cartItemDao;
    }

    public Order save(final Order order) {
        OrderDto orderDto = OrderDto.from(order);
        Long orderId = orderDao.insert(orderDto);
        List<OrderProduct> orderProductsAfterSave = saveOrderProducts(orderId, order.getOrderProducts());

        return new Order(
                orderId,
                order.getTimeStamp(),
                order.getMember(),
                order.getCoupon(),
                orderProductsAfterSave
        );
    }

    private List<OrderProduct> saveOrderProducts(final Long orderId, final List<OrderProduct> orderProducts) {
        List<OrderProduct> orderProductsAfterSave = new LinkedList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductDto orderProductDto = OrderProductDto.of(orderId, orderProduct);
            Long insert = orderProductDao.insert(orderProductDto);
            OrderProduct orderProductHasId = new OrderProduct(insert, orderProduct.getProduct(), orderProduct.getQuantity());
            orderProductsAfterSave.add(orderProductHasId);
        }
        return orderProductsAfterSave;
    }

    public List<CartItem> findCartItemById(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toList());
    }

    public void deleteCartItems(List<Long> cartItemIds) {
        for (Long cartItemId : cartItemIds) {
            deleteCartItem(cartItemId);
        }
    }

    private void deleteCartItem(final Long cartItemId) {
        int deleteCount = cartItemDao.deleteById(cartItemId);

        if (deleteCount == 0) {
            throw new IllegalArgumentException("장바구니가 삭제되지 않았습니다.");
        }
    }

}
