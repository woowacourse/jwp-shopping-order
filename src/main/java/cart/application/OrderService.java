package cart.application;

import cart.dao.OrderItemDao;
import cart.dao.OrdersDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.CartItemDto;
import cart.dto.OrderCreateResponse;
import cart.dto.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CouponService couponService;
    private final OrdersDao ordersDao;
    private final OrderItemDao orderItemDao;

    public OrderService(CouponService couponService, OrdersDao ordersDao, OrderItemDao orderItemDao) {
        this.couponService = couponService;
        this.ordersDao = ordersDao;
        this.orderItemDao = orderItemDao;
    }

    public OrderCreateResponse add(Member member, OrderRequest orderRequest) {
        int originalPrice = calculateOriginalPrice(orderRequest);
        Coupon coupon = couponService.findById(orderRequest.getCouponIds().get(0));
        int actualPrice = coupon.calculateActualPrice(originalPrice);
        Order orderToAdd = new Order(member.getId(), originalPrice, actualPrice, orderRequest.getDeliveryFee());
        long orderId = ordersDao.save(orderToAdd);
        addOrderItems(orderId, orderRequest);
        return new OrderCreateResponse(orderId);
    }

    private int calculateOriginalPrice(OrderRequest orderRequest) {
        return orderRequest.getCartItems().stream()
                .mapToInt(cartItemDto -> cartItemDto.getProduct().getPrice())
                .sum();
    }

    private void addOrderItems(long orderId, OrderRequest orderRequest) {
        for (CartItemDto cartItemDto : orderRequest.getCartItems()) {
            OrderItem orderItemToAdd = new OrderItem(
                    orderId,
                    cartItemDto.getProduct().getId(),
                    cartItemDto.getProduct().getName(),
                    cartItemDto.getProduct().getImageUrl(),
                    cartItemDto.getProduct().getPrice(),
                    cartItemDto.getQuantity()
            );
            orderItemDao.save(orderItemToAdd);
        }
    }
}
