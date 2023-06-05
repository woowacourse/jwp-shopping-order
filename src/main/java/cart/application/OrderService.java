package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.*;
import cart.dto.OrderDto;
import cart.dto.OrderItemDto;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final DeliveryFeeCalculator deliveryFeeCalculator;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final CouponService couponService;
    private final PayService payService;

    public OrderService(DeliveryFeeCalculator deliveryFeeCalculator, CartItemDao cartItemDao, OrderDao orderDao, CouponService couponService, PayService payService) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.couponService = couponService;
        this.payService = payService;
    }

    @Transactional
    public long order(Member member, OrderRequest orderRequest) {
        List<CartItem> cartItems = cartItemDao.findItemsByIds(orderRequest.getCartItemIds());
        Order order = Order.of(cartItems, member, orderRequest.getTotalPrice());

        Money deliveryFee = deliveryFeeCalculator.calculate(order);

        Money discountedMoney = applyDiscounting(orderRequest, order);

        Order payed = payService.pay(order, deliveryFee, discountedMoney, member.getId());

        cartItemDao.deleteByIds(orderRequest.getCartItemIds());

        couponService.addCouponDependsOnPay(member, payed);
        return orderDao.save(payed, discountedMoney);
    }

    private Money applyDiscounting(OrderRequest orderRequest, Order order) {
        Money discounting = Money.from(0);
        if (isCouponSelected(orderRequest)) {
            discounting = couponService.apply(order, orderRequest.getCouponId());
        }
        return discounting;
    }

    private boolean isCouponSelected(OrderRequest orderRequest) {
        return (!orderRequest.isCouponNull()) && (!orderRequest.getCouponId().equals(0L));
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrder(Member member, Long orderId) {
        OrderDto order = orderDao.findById(orderId);
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderItemDto> findOrderByMember(Member member) {
        List<OrderDto> orders = orderDao.findByMemberId(member.getId());
        return orders.stream().map(orderDto -> OrderItemDto.of(orderDto.getId(), orderDto.getCartItems()))
                .collect(Collectors.toList());
    }
}
