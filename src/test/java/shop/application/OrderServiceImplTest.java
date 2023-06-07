package shop.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shop.application.coupon.CouponService;
import shop.application.order.OrderService;
import shop.application.order.dto.*;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.CouponType;
import shop.domain.member.Member;
import shop.domain.repository.CouponRepository;
import shop.domain.repository.MemberRepository;
import shop.domain.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceImplTest extends ServiceTest {
    private Long pizzaId;
    private Long chickenId;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderService orderService;


    @BeforeEach
    void setUp() {
        memberRepository.save(ServiceTestFixture.member);
        pizzaId = productRepository.save(ServiceTestFixture.pizza);
        chickenId = productRepository.save(ServiceTestFixture.chicken);
    }

    @DisplayName("상품을 주문할 수 있다.")
    @Test
    void orderTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());

        OrderCreationDto orderCreationDto =
                OrderCreationDto.of(List.of(
                                new OrderProductDto(pizzaId, 5),
                                new OrderProductDto(chickenId, 10)),
                        null);

        //when
        Long orderId = orderService.order(findMember, orderCreationDto);

        //then
        OrderDetailDto orderDetail = orderService.getOrderDetailsOfMember(findMember, orderId);
        List<OrderItemDto> orderItems = orderDetail.getOrder().getOrderItems();

        assertThat(orderDetail.getCoupon()).isNull();
        assertThat(orderItems).extractingResultOf("getQuantity")
                .containsExactlyInAnyOrder(5, 10);
        assertThat(orderItems).extractingResultOf("getProduct").extractingResultOf("getName")
                .containsExactlyInAnyOrder("피자", "치킨");
    }

    @DisplayName("쿠폰을 사용하여 상품을 주문할 수 있다.")
    @Test
    void orderWithCouponTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());
        Coupon coupon = couponRepository.findByCouponType(CouponType.WELCOME_JOIN);
        couponService.issueCoupon(findMember.getId(), CouponType.WELCOME_JOIN);

        OrderCreationDto orderCreationDto =
                OrderCreationDto.of(List.of(
                                new OrderProductDto(pizzaId, 5),
                                new OrderProductDto(chickenId, 10)),
                        coupon.getId());

        //when
        Long orderId = orderService.order(findMember, orderCreationDto);

        //then
        OrderDetailDto orderDetail = orderService.getOrderDetailsOfMember(findMember, orderId);
        List<OrderItemDto> orderItems = orderDetail.getOrder().getOrderItems();

        assertThat(orderDetail.getCoupon().getName()).isEqualTo(CouponType.WELCOME_JOIN.getName());
        assertThat(orderDetail.getCoupon().getDiscountRate()).isEqualTo(CouponType.WELCOME_JOIN.getDiscountRate());
        assertThat(orderItems).extractingResultOf("getQuantity")
                .containsExactlyInAnyOrder(5, 10);
        assertThat(orderItems).extractingResultOf("getProduct").extractingResultOf("getName")
                .containsExactlyInAnyOrder("피자", "치킨");
    }

    @DisplayName("모든 주문내역을 조회할 수 있다.")
    @Test
    void getAllorderHistoryTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());

        OrderCreationDto orderCreationDto =
                OrderCreationDto.of(List.of(
                                new OrderProductDto(pizzaId, 5),
                                new OrderProductDto(chickenId, 10)),
                        null);

        //when
        orderService.order(findMember, orderCreationDto);
        orderService.order(findMember, orderCreationDto);

        //then
        List<OrderDto> allOrders = orderService.getAllOrderHistoryOfMember(findMember);

        assertThat(allOrders.size()).isEqualTo(2);
        assertThat(allOrders).extracting("orderItems").extractingResultOf("product")
                .extractingResultOf("getName")
                .containsExactlyInAnyOrder("피자", "치킨", "피자", "치킨");
    }
}
