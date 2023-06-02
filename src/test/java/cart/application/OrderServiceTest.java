package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderDetail;
import cart.dto.response.OrderInfo;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.CartItemCalculateException;
import cart.exception.CartItemNotFoundException;
import cart.exception.CouponDiscountOverPriceException;
import cart.exception.CouponNotFoundException;
import cart.exception.OrderAuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private CouponDao couponDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("존재하지 않는 카트 아이템에 대한 요청을 보내면 예외가 발생한다.")
    @Test
    void order_invalid_notExistCartItem() {
        //given
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, null, null),
                        new CartItem(2L, 2, null, null)
                ));

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L, 3L), 1000, 1L);

        //when, then
        assertThatThrownBy(() -> orderService.order(member, orderRequestDto))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @DisplayName("존재하지 않는 쿠폰을 적용하면 예외가 발생한다.")
    @Test
    void order_invalid_notExistCoupon() {
        //given
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, new Product("name1", 100, "image1"), null),
                        new CartItem(2L, 2, new Product("name2", 200, "image2"), null)
                ));
        given(couponDao.findCouponById(anyLong()))
                .willReturn(List.of(
                        new Coupon(1L, 1000, "1000"),
                        new Coupon(2L, 2000, "2000")));

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L), 1000, 3L);

        //when, then
        assertThatThrownBy(() -> orderService.order(member, orderRequestDto))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @DisplayName("쿠폰이 없을 경우에도 요청은 성공한다.")
    @Test
    void order_valid_exceptCoupon() {
        //given
        final Member member = new Member(1L, "email", "password");
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, new Product("name1", 100, "image1"), member),
                        new CartItem(2L, 2, new Product("name2", 200, "image2"), member)
                ));

        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L), 3100, null);

        //when, then
        assertDoesNotThrow(() -> orderService.order(member, orderRequestDto));
    }

    @DisplayName("쿠폰의 할인 금액이 더 클 경우 예외가 발생한다.")
    @Test
    void order_invalid_couponOverDiscount() {
        //given
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, new Product("name1", 100, "image1"), null),
                        new CartItem(2L, 2, new Product("name2", 200, "image2"), null)
                ));

        given(couponDao.findCouponById(anyLong()))
                .willReturn(List.of(
                        new Coupon(1L, 10000, "1000"),
                        new Coupon(2L, 20000, "2000")));

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L), 1000, 1L);

        //when, then
        assertThatThrownBy(() -> orderService.order(member, orderRequestDto))
                .isInstanceOf(CouponDiscountOverPriceException.class);
    }

    @DisplayName("사용자가 예상한 계산 금액과 실 계산 금액이 다르면 예외가 발생한다.")
    @Test
    void order_invalid_calculateMismatch() {
        //given
        final int expectProductPrice = 100;
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, new Product("name1", expectProductPrice, "image1"), null),
                        new CartItem(2L, 2, new Product("name2", 200, "image2"), null)
                ));

        final int expectCouponDiscount = 2000;
        given(couponDao.findCouponById(anyLong()))
                .willReturn(List.of(
                        new Coupon(1L, 1000, "1000"),
                        new Coupon(2L, expectCouponDiscount, "2000")));

        final int defaultDeliveryFee = 3000;
        final int wrongCount = 100;

        final int expectPrice = defaultDeliveryFee + expectProductPrice - expectCouponDiscount + wrongCount;

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L), expectPrice, 2L);

        //when, then
        assertThatThrownBy(() -> orderService.order(member, orderRequestDto))
                .isInstanceOf(CartItemCalculateException.class);
    }

    @DisplayName("사용자가 예상한 계산 금액과 실 계산 금액이 다르면 예외가 발생한다.")
    @Test
    void order_valid_match() {
        //given
        final int expectProductPrice = 100;
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, new Product("name1", expectProductPrice, "image1"), null),
                        new CartItem(2L, 2, new Product("name2", 200, "image2"), null)
                ));

        final int expectCouponDiscount = 2000;
        given(couponDao.findCouponById(anyLong()))
                .willReturn(List.of(
                        new Coupon(1L, 1000, "1000"),
                        new Coupon(2L, expectCouponDiscount, "2000")));

        final int defaultDeliveryFee = 3000;
        final int wrongCount = 100;

        final int expectPrice = defaultDeliveryFee + expectProductPrice - expectCouponDiscount;

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L), expectPrice, 2L);

        //when, then
        assertDoesNotThrow(() -> orderService.order(member, orderRequestDto));
    }

    @DisplayName("사용자의 주문 목록을 가져온다.")
    @Test
    void findOrderOf() {
        //given
        final Member member = new Member(1L, "email", "password");

        given(orderDao.findOrderByMemberId(anyLong()))
                .willReturn(List.of(
                        new OrderEntity(1L, 1L, 3000),
                        new OrderEntity(2L, 1L, 2000)
                ));

        given(orderDao.findOrderProductByIds(anyList()))
                .willReturn(List.of(
                        new OrderItemEntity(1L, 1L, 1L, 1),
                        new OrderItemEntity(2L, 1L, 2L, 2),
                        new OrderItemEntity(3L, 2L, 1L, 3),
                        new OrderItemEntity(4L, 2L, 2L, 4)
                ));

        given(productDao.getProductByIds(anyList()))
                .willReturn(List.of(
                                new Product(1L, "productA", 1000, "image"),
                                new Product(2L, "productB", 2000, "image")
                        )
                );

        //when
        final List<OrderInfo> orders = orderService.findOrderOf(member);

        //then
        assertAll(
                () -> assertThat(orders).hasSize(2),
                () -> assertThat(orders.get(0).getOrderId()).isEqualTo(1L),
                () -> assertThat(orders.get(0).getOrderItems()).hasSize(2),
                () -> assertThat(orders.get(0).getOrderItems().get(0).getId()).isEqualTo(1L),
                () -> assertThat(orders.get(0).getOrderItems().get(0).getQuantity()).isEqualTo(1),
                () -> assertThat(orders.get(0).getOrderItems().get(0).getProduct().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("자신의 주문이 아닌 것의 상세정보를 가져올 수 없다.")
    @Test
    void findOrderDetail_invalid_unAuthorization() {
        //given
        final long memberId = 5L;
        final Member member = new Member(memberId, "email", "password");


        final long orderOwnerId = 1L;
        given(orderDao.findById(anyLong()))
                .willReturn(new OrderEntity(1L, orderOwnerId, 10000));

        //when, then
        assertThatThrownBy(() -> orderService.findOrderDetail(member, 1L))
                .isInstanceOf(OrderAuthorizationException.class);
    }

    @DisplayName("주문 내역 디테일을 가져온다.")
    @Test
    void findOrderDetail() {
        //given
        final Member member = new Member(1L, "email", "password");
        given(orderDao.findById(anyLong()))
                .willReturn(new OrderEntity(1L, 1L, 10000));
        given(orderDao.findOrderProductByOrderId(1L))
                .willReturn(List.of(
                        new OrderItemEntity(1L, 1L, 1L, 3),
                        new OrderItemEntity(2L, 1L, 2L, 4)
                ));
        given(productDao.getProductByIds(anyList()))
                .willReturn(List.of(
                        new Product(1L, "productA", 1000, "imageA"),
                        new Product(2L, "productB", 2000, "imageB")
                ));

        //when
        final OrderDetail orderDetail = orderService.findOrderDetail(member, 1L);

        //then
        assertAll(
                () -> assertThat(orderDetail.getTotalPrice()).isEqualTo(10000),
                () -> assertThat(orderDetail.getOrder().getOrderItems()).hasSize(2),
                () -> assertThat(orderDetail.getOrder().getOrderId()).isEqualTo(1L),
                () -> assertThat(orderDetail.getOrder().getOrderItems()).hasSize(2),
                () -> assertThat(orderDetail.getOrder().getOrderItems().get(0).getQuantity()).isEqualTo(3),
                () -> assertThat(orderDetail.getOrder().getOrderItems().get(0).getId()).isEqualTo(1L),
                () -> assertThat(orderDetail.getOrder().getOrderItems().get(0).getProduct().getId()).isEqualTo(1L)
        );
    }
}
