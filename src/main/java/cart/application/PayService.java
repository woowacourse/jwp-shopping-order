package cart.application;

import cart.application.dto.request.PaymentRequest;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProducts;
import cart.persistence.repository.PayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PayService {

    private final PayRepository payRepository;

    public PayService(final PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    @Transactional
    public Long pay(final Member member, final PaymentRequest request) {
        final List<Long> cartIds = request.getCartItemIds();
        final List<CartItem> cartItems = getCartItems(member, cartIds);
        member.usePoint(request.getPoint());
        final OrderProducts orderProducts = new OrderProducts(cartItems);
        final Order order = new Order(member, request.getPoint(), orderProducts);
        payRepository.deleteCartItemsByIds(cartIds);
        return payRepository.createOrder(order);
    }

    private List<CartItem> getCartItems(final Member member, final List<Long> cartIds) {
        final List<CartItem> cartItems = payRepository.getCartItemsByIds(cartIds);
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
        return cartItems;
    }
}
