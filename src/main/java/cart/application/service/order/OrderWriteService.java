package cart.application.service.order;

import cart.application.repository.cartItem.CartItemRepository;
import cart.application.repository.coupon.CouponRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.application.repository.point.PointRepository;
import cart.application.service.order.dto.CreateOrderDto;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.point.PointHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderWriteService {

    private final OrderRepository orderRepository;
    private final OrderedItemRepository orderedItemRepository;
    private final OrderMakeService orderMakeService;
    private final CouponRepository couponRepository;
    private final PointRepository pointRepository;
    private final CartItemRepository cartItemRepository;

    public OrderWriteService(
            final OrderRepository orderRepository,
            final OrderedItemRepository orderedItemRepository,
            final OrderMakeService orderMakeService,
            final CouponRepository couponRepository,
            final PointRepository pointRepository,
            final CartItemRepository cartItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.orderMakeService = orderMakeService;
        this.couponRepository = couponRepository;
        this.pointRepository = pointRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Long createOrder(final Member member, final CreateOrderDto createOrderDto) {
        final Order order = orderMakeService.makeOrder(member, createOrderDto);
        final Long orderId = orderRepository.createOrder(order);

        orderedItemRepository.createOrderItems(orderId, order.getOrderItems());
        pointRepository.createPointHistory(member.getId(), new PointHistory(orderId, order.getSavePoint(), order.getPoint()));
        changeCouponState(order.getCoupons(), orderId);
        createOrderDto.getCreateOrderItemDtos()
                .forEach(createOrderItemDto -> cartItemRepository.deleteById(createOrderItemDto.getCartItemId()));

        return orderId;
    }

    private void changeCouponState(final Coupons coupons, final Long orderId) {
        coupons.getCoupons()
                .forEach(coupon -> {
                    couponRepository.convertToUseMemberCoupon(coupon);
                    couponRepository.createOrderedCoupon(orderId, coupon);
                });
    }
}
