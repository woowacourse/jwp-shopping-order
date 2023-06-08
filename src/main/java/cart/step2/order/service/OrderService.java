package cart.step2.order.service;

import cart.step2.coupon.domain.repository.CouponRepository;
import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import cart.step2.order.domain.Order;
import cart.step2.order.domain.repository.OrderRepository;
import cart.step2.order.presentation.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final CouponRepository couponRepository;
    private final CouponTypeRepository couponTypeRepository;
    private final OrderRepository orderRepository;

    public OrderService(final CouponRepository couponRepository, final CouponTypeRepository couponTypeRepository, final OrderRepository orderRepository) {
        this.couponRepository = couponRepository;
        this.couponTypeRepository = couponTypeRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long create(final Order order) {
        validateFinalPrice(order);
        return orderRepository.save(order);
    }

    private void validateFinalPrice(final Order order) {
        Long couponTypeId = couponRepository.findById(order.getCouponId()).getCouponTypeId();
        CouponType couponType = couponTypeRepository.findById(couponTypeId);
        order.validatePrice(couponType.getDiscountAmount());
    }

    public List<OrderResponse> findAllByMemberId(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(order -> new OrderResponse(order.getId(), order.getOrderItems(), order.getCreatedAt(), order.getPrice()))
                .collect(Collectors.toList());
    }

}
