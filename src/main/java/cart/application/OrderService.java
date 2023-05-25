package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderedItemDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.discount.PriceCalculator;
import cart.dto.OrderRequest;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;
    private final OrderedItemDao orderedItemDao;
    private final PriceCalculator priceCalculator;

    public OrderService(
            OrderDao orderDao,
            CartItemDao cartItemDao,
            OrderedItemDao orderedItemDao,
            PriceCalculator priceCalculator
    ) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.orderedItemDao = orderedItemDao;
        this.priceCalculator = priceCalculator;
    }

    @Transactional
    public Long order(OrderRequest orderRequest, Member member) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final CartItems cartItems = new CartItems(getCartItems(cartItemIds));
        cartItems.validateAllCartItemsBelongsToMember(member);

        final Integer totalPrice = cartItems.calculatePriceSum();
        final Integer finalPrice = priceCalculator.calculateFinalPrice(totalPrice, member);

        final Order order = new Order(finalPrice, member, cartItems.getCartItems());
        final Long orderId = orderDao.addOrder(order);

        orderedItemDao.saveAll(cartItems.getCartItems(), orderId);
        cartItemDao.deleteByIds(cartItemIds);

        return orderId;
    }

    private List<CartItem> getCartItems(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemDao::findById)
                .collect(Collectors.toUnmodifiableList());
    }
}
