package cart.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cart.application.dto.order.CreateOrderByCartItemsRequest;
import cart.application.dto.order.OrderProductRequest;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberCouponRepository;
import cart.application.repository.OrderRepository;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.CouponType;
import cart.domain.coupon.MemberCoupon;
import cart.exception.badrequest.BadRequestErrorType;
import cart.exception.badrequest.BadRequestException;
import cart.exception.forbidden.ForbiddenException;
import cart.exception.noexist.NoExistErrorType;
import cart.exception.noexist.NoExistException;
import cart.fixture.CouponFixture.금액_10000원이상_1000원할인;
import cart.fixture.MemberFixture.Member_test1;
import cart.fixture.MemberFixture.Member_test2;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private CartItemRepository cartItemRepository;
    private MemberCouponRepository memberCouponRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private long memberId;
    private Member member;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        member = Member_test1.getMemberOf(memberId);
        cartItemRepository = Mockito.mock(CartItemRepository.class);
        memberCouponRepository = Mockito.mock(MemberCouponRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        orderService = new OrderService(cartItemRepository, memberCouponRepository, orderRepository);

    }

    @Nested
    class 카트_아이템_주문시_ {

        int quantity = 1;
        long cartItemId = 1L;
        int price = 1000;
        long memberCouponId = 1L;
        private CreateOrderByCartItemsRequest request;
        private LocalDateTime futureDate;
        private Member other;

        @BeforeEach
        void setUp() {
            request = new CreateOrderByCartItemsRequest(memberCouponId, List.of(
                    new OrderProductRequest(cartItemId, quantity, "pizza", price, "pizzaImage")));
            other = Member_test2.getMemberOf(memberId + 1L);

            given(cartItemRepository.findByIds(any()))
                    .willReturn(new CartItems(
                            List.of(new CartItem(cartItemId, quantity, new Product(1L, "pizza", price, "pizzaImage"),
                                    member))));

            futureDate = LocalDateTime.of(9999, 12, 31, 0, 0);
            given(memberCouponRepository.findById(memberCouponId))
                    .willReturn(Optional.of(
                            new MemberCoupon(memberCouponId, 금액_10000원이상_1000원할인.COUPON, member, false, futureDate,
                                    LocalDateTime.now())));
        }

        @Nested
        class 요청과_DB_상태_비교 {

            @Test
            void 요청_수량과_DB_수량이_다르면_예외() {
                // given
                given(cartItemRepository.findByIds(any()))
                        .willReturn(new CartItems(
                                List.of(new CartItem(cartItemId, quantity + 1,
                                        new Product(1L, "pizza", 1000, "pizzaImage"), member))));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(BadRequestErrorType.CART_ITEM_QUANTITY_INCORRECT.name());
            }

            @Test
            void 요청_가격과_DB_가격이_다르면_예외() {
                // given
                given(cartItemRepository.findByIds(any()))
                        .willReturn(new CartItems(
                                List.of(new CartItem(cartItemId, 1, new Product(1L, "pizza", price * 2, "pizzaImage"),
                                        member))));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(BadRequestErrorType.CART_ITEM_PRICE_INCORRECT.name());
            }
        }

        @Nested
        class 장바구니_검증 {

            @Test
            void 본인의_장바구니_물품이_아니면_예외() {
                // given
                given(cartItemRepository.findByIds(any()))
                        .willReturn(new CartItems(
                                List.of(new CartItem(cartItemId, 1,
                                        new Product(1L, "pizza", 1000, "pizzaImage"), other))));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(ForbiddenException.class);
            }

            @Test
            void 장바구니_상품이_비어있으면_예외() {
                // given
                given(cartItemRepository.findByIds(any()))
                        .willReturn(new CartItems(Collections.emptyList()));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(BadRequestErrorType.CART_ITEM_EMPTY.name());
            }

            @Test
            void 장바구니_상품이_하나라도_없으면_예외() {
                // given
                request = new CreateOrderByCartItemsRequest(memberCouponId, List.of(
                        new OrderProductRequest(cartItemId, quantity, "pizza", price, "pizzaImage"),
                        new OrderProductRequest(cartItemId + 1L, quantity, "chicken", price, "chickenImage")));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(NoExistException.class)
                        .hasMessage(NoExistErrorType.CART_ITEM_NO_EXIST.name());
            }
        }

        @Nested
        class 쿠폰_검증 {

            @Test
            void 해당_id의_쿠폰이_없으면_예외() {
                // given
                given(memberCouponRepository.findById(memberCouponId))
                        .willReturn(Optional.empty());

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(NoExistException.class)
                        .hasMessage(NoExistErrorType.COUPON_NO_EXIST.name());
            }

            @Test
            void 본인의_쿠폰이_아니면_예외() {
                // given
                given(memberCouponRepository.findById(memberCouponId))
                        .willReturn(Optional.of(
                                new MemberCoupon(memberCouponId, 금액_10000원이상_1000원할인.COUPON, other, false, futureDate,
                                        LocalDateTime.now())));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(ForbiddenException.class);
            }

            @Test
            void 유효기간이_만료된_쿠폰이면_예외() {
                // given
                given(memberCouponRepository.findById(memberCouponId))
                        .willReturn(Optional.of(
                                new MemberCoupon(memberCouponId, 금액_10000원이상_1000원할인.COUPON, member, false,
                                        LocalDateTime.MIN, LocalDateTime.now())));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(BadRequestErrorType.COUPON_UNAVAILABLE.name());
            }

            @Test
            void 이미_사용된_쿠폰이면_예외() {
                // given
                given(memberCouponRepository.findById(memberCouponId))
                        .willReturn(Optional.of(
                                new MemberCoupon(memberCouponId, 금액_10000원이상_1000원할인.COUPON, member, true, futureDate,
                                        LocalDateTime.now())));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(BadRequestErrorType.COUPON_UNAVAILABLE.name());
            }

            @Test
            void 최소적용금액이_맞지않는_쿠폰이면_예외() {
                // given
                given(memberCouponRepository.findById(memberCouponId))
                        .willReturn(Optional.of(
                                new MemberCoupon(memberCouponId,
                                        new Coupon(1L, new CouponInfo("100,000원 이상 적용", 100_000, 1000), 1000,
                                                CouponType.FIXED_AMOUNT),
                                        member, false, futureDate,
                                        LocalDateTime.now())));

                // when then
                assertThatThrownBy(() -> orderService.orderCartItems(member, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage(BadRequestErrorType.COUPON_UNAVAILABLE.name());
            }
        }
    }
}
