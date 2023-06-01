package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.*;
import cart.dto.OrderDto;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final DeliveryPolicy deliveryPolicy;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final CouponService couponService;
    private final PayService payService;

    public OrderService(DeliveryPolicy deliveryPolicy, CartItemDao cartItemDao, OrderDao orderDao, CouponService couponService, PayService payService) {
        this.deliveryPolicy = deliveryPolicy;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.couponService = couponService;
        this.payService = payService;
    }

    @Transactional
    public long order(Member member, OrderRequest orderRequest) {
        List<CartItem> cartItems = cartItemDao.findItemsByIds(orderRequest.getCartItemIds());
        Order order = Order.of(cartItems, member, orderRequest.getPrice());

        Money deliveryFee = deliveryPolicy.calculate(order);
        Money discounting = couponService.apply(order, orderRequest.getCouponId());

        Order payed = payService.pay(order, deliveryFee, discounting, member.getId());

        cartItemDao.deleteByIds(orderRequest.getCartItemIds());
        return orderDao.save(payed, discounting);
    }

    public OrderResponse findOrder(Member member, Long orderId) {
        OrderDto order = orderDao.findById(orderId);
        // TODO: 2023-06-02 order가 member의 것인지 검증
        return new OrderResponse(order.getId(), order.getCartItems(), order.getPrice());
    }
}
