package shop.application.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.order.dto.OrderDetailDto;
import shop.application.order.dto.OrderCreationDto;
import shop.application.order.dto.OrderProductDto;
import shop.domain.cart.Quantity;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.MemberCoupon;
import shop.domain.member.Member;
import shop.domain.order.Order;
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
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(OrderRepository orderRepository, CouponRepository couponRepository,
                        ProductRepository productRepository, MemberCouponRepository memberCouponRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.productRepository = productRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional
    public Long order(Member member, OrderCreationDto orderCreationDto) {
        List<OrderItem> orderItems = createOrderItems(member, orderCreationDto);
        OrderPrice orderPrice = calculateOrderPrice(member, orderCreationDto.getCouponId(), orderItems);

        Order order = new Order(orderItems, orderPrice, LocalDateTime.now());

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

        MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(member.getId(), couponId);
        memberCoupon.checkAvailability();

        Coupon coupon = couponRepository.findById(couponId);
        memberCouponRepository.updateCouponUsingStatus(memberCoupon.getId(), true);
        return OrderPrice.createFromItemsWithCoupon(orderItems, coupon);
    }

    public List<Order> getAllOrderHistoryOfMember(Member member) {
        List<Order> orders = orderRepository.findAllByMember(member);
        orders.forEach(order -> order.checkOwner(member));

        return orders;
    }

    public OrderDetailDto getOrderDetailsOfMember(Member member, Long orderId) {
        OrderDetailDto orderDetailDto = orderRepository.findDetailsByMemberAndOrderId(member, orderId);
        orderDetailDto.getOrder().checkOwner(member);

        return orderDetailDto;
    }
}
