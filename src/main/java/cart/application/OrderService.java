package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final DiscountService discountService;

    public OrderService(final CartItemDao cartItemDao, final OrderDao orderDao, final DiscountService discountService) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.discountService = discountService;
    }

    @Transactional
    public Long save(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(orderRequest.getCartItemIds());
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));

        final int totalPrice = discountService.calculateTotalPrice(member.getGrade(), sumPrices(cartItems));
        final Order order = new Order(member, totalPrice, cartItems);

        final Long orderId = orderDao.save(order).getId();
        cartItemDao.deleteAllByIds(cartItemsToIds(cartItems));

        return orderId;
    }

    private int sumPrices(final List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    private List<Long> cartItemsToIds(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(toList());
    }

    public List<OrderResponse> findAllByMember(final Member member) {
        final List<Order> orders = orderDao.findAllByMemberId(member.getId());
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    public OrderResponse findById(final Long id) {
        final Order order = orderDao.findById(id);
        return OrderResponse.from(order);
    }
}
