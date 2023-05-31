package cart.application;

import cart.application.dto.request.CartItemRequest;
import cart.application.dto.request.PaymentRequest;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProducts;
import cart.persistence.dao.OrderHistoryDao;
import cart.persistence.dao.OrderProductDao;
import cart.persistence.repository.PayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayService {

    private final PayRepository payRepository;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;

    public PayService(final PayRepository payRepository, final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao) {
        this.payRepository = payRepository;
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
    }

    @Transactional
    public Long payment(final Member member, final PaymentRequest request) {
        final List<Long> cartIds = extractCartIds(request);
        final List<CartItem> cartItems = getCartItems(member, cartIds);
        member.usePoint(request.getPoint());
        final OrderProducts orderProducts = new OrderProducts(cartItems);
        final Order order = new Order(null, member, request.getPoint(), orderProducts);
        final Long orderId = orderHistoryDao.createOrder(order);
        member.savePoint(order.getSavedPoint());
        orderProductDao.createProducts(orderId, order.getOrderProducts());
        payRepository.updatePoint(member);
        return orderId;
    }

    private List<Long> extractCartIds(final PaymentRequest request) {
        return request.getCartItemRequests().stream()
                .map(CartItemRequest::getProductId)
                .collect(Collectors.toList());
    }

    private List<CartItem> getCartItems(final Member member, final List<Long> cartIds) {
        final List<CartItem> cartItems = payRepository.getCartItemsByIds(cartIds);
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
        return cartItems;
    }
}
