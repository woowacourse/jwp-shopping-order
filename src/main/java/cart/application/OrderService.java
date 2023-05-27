package cart.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.discount.PriceCalculator;
import cart.dto.OrderRequest;
import cart.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final CartItemDao cartItemDao;
    private final PriceCalculator priceCalculator;
    private final OrderRepository orderRepository;

    public OrderService(
            CartItemDao cartItemDao,
            PriceCalculator priceCalculator,
            OrderRepository orderRepository
    ) {
        this.cartItemDao = cartItemDao;
        this.priceCalculator = priceCalculator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long order(OrderRequest orderRequest, Member member) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final CartItems cartItems = new CartItems(cartItemDao.findByIds(cartItemIds));
        cartItems.validateAllCartItemsBelongsToMember(member);
        cartItems.validateExistentCartItems(cartItemIds);

        final Integer totalPrice = cartItems.calculatePriceSum();
        final Integer finalPrice = priceCalculator.calculateFinalPrice(totalPrice, member);

        return orderRepository.saveOrder(cartItemIds, member, finalPrice, cartItems);
    }
}
