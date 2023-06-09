package shop.application.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.order.dto.OrderCreationDto;
import shop.application.order.dto.OrderDetailDto;
import shop.application.order.dto.OrderDto;
import shop.application.order.dto.OrderProductDto;
import shop.domain.cart.Quantity;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.MemberCoupon;
import shop.domain.member.Member;
import shop.domain.order.Order;
import shop.domain.order.OrderDetail;
import shop.domain.order.OrderItem;
import shop.domain.order.OrderPrice;
import shop.domain.product.Product;
import shop.domain.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderServiceImpl(CartRepository cartRepository, OrderRepository orderRepository,
                            ProductRepository productRepository, CouponRepository couponRepository,
                            MemberCouponRepository memberCouponRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @Transactional
    @Override
    public Long order(Member member, OrderCreationDto orderCreationDto) {
        List<OrderItem> orderItems = createOrderItems(member, orderCreationDto);
        OrderPrice orderPrice = calculateOrderPrice(member, orderCreationDto.getCouponId(), orderItems);

        Order order = new Order(orderItems, orderPrice, LocalDateTime.now());
        Long orderId = orderRepository.save(member.getId(), orderCreationDto.getCouponId(), order);

        List<Long> productIds = getProductIds(orderItems);
        cartRepository.deleteByMemberIdAndProductIds(member.getId(), productIds);

        return orderId;
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

    private List<Long> getProductIds(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getProduct)
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrderHistoryOfMember(Member member) {
        List<Order> orders = orderRepository.findAllByMember(member);

        return OrderDto.of(orders);
    }

    @Override
    public OrderDetailDto getOrderDetailsOfMember(Member member, Long orderId) {
        OrderDetail orderDetail = orderRepository.findDetailsByOrderId(orderId);
        orderDetail.getOrder().checkOwner(member);

        return OrderDetailDto.of(orderDetail);
    }
}
