package shop.application.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.order.dto.OrderCreationDto;
import shop.application.order.dto.OrderDetailDto;
import shop.application.order.dto.OrderDto;
import shop.application.order.dto.OrderProductDto;
import shop.domain.cart.Quantity;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.MemberCoupon;
import shop.domain.event.ProductOrderedEvent;
import shop.domain.member.Member;
import shop.domain.order.Order;
import shop.domain.order.OrderDetail;
import shop.domain.order.OrderItem;
import shop.domain.order.OrderPrice;
import shop.domain.product.Product;
import shop.domain.repository.CouponRepository;
import shop.domain.repository.MemberCouponRepository;
import shop.domain.repository.OrderRepository;
import shop.domain.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(ApplicationEventPublisher eventPublisher, OrderRepository orderRepository,
                        ProductRepository productRepository, CouponRepository couponRepository,
                        MemberCouponRepository memberCouponRepository) {
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional
    public Long order(Member member, OrderCreationDto orderCreationDto) {
        List<OrderItem> orderItems = createOrderItems(member, orderCreationDto);
        OrderPrice orderPrice = calculateOrderPrice(member, orderCreationDto.getCouponId(), orderItems);

        Order order = new Order(orderItems, orderPrice, LocalDateTime.now());
        eventPublisher.publishEvent(new ProductOrderedEvent(member.getId(), orderItems));
        return orderRepository.save(member.getId(), orderCreationDto.getCouponId(), order);
    }

    private List<OrderItem> createOrderItems(Member member, OrderCreationDto orderCreationDto) {
        return orderCreationDto.getOrderItemDtos().stream()
                .map(orderProductDto -> createOrderItem(member, orderProductDto))
                .collect(Collectors.toList());
    }

    private OrderItem createOrderItem(Member member, OrderProductDto orderProductDto) {
        Long productId = orderProductDto.getProductId();
        Product product = productRepository.findById(productId);
        Quantity quantity = new Quantity(orderProductDto.getQuantity());

        return new OrderItem(product, quantity, member);
    }

    private OrderPrice calculateOrderPrice(Member member, Long couponId, List<OrderItem> orderItems) {
        if (couponId == null) {
            return OrderPrice.createFromItems(orderItems);
        }

        MemberCoupon memberCoupon =
                memberCouponRepository.findByMemberIdAndCouponId(member.getId(), couponId);
        memberCoupon.checkAvailability();

        Coupon coupon = couponRepository.findById(couponId);
        memberCouponRepository.updateCouponUsingStatus(memberCoupon.getId(), true);

        return OrderPrice.createFromItemsWithCoupon(orderItems, coupon);
    }

    public List<OrderDto> getAllOrderHistoryOfMember(Member member) {
        List<Order> orders = orderRepository.findAllByMember(member);
        orders.forEach(order -> order.checkOwner(member));

        return OrderDto.of(orders);
    }

    public OrderDetailDto getOrderDetailsOfMember(Member member, Long orderId) {
        OrderDetail orderDetail = orderRepository.findDetailsByMemberAndOrderId(member, orderId);
        orderDetail.getOrder().checkOwner(member);

        return OrderDetailDto.of(orderDetail);
    }
}
