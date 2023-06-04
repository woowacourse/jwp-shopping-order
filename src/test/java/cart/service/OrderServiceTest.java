package cart.service;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.밀리_만료기간_지난_쿠폰_10퍼센트;
import static cart.fixture.TestFixture.밀리_인증_정보;
import static cart.fixture.TestFixture.밀리_쿠폰_10퍼센트;
import static cart.fixture.TestFixture.박스터;
import static cart.fixture.TestFixture.박스터_인증_정보;
import static cart.fixture.TestFixture.장바구니_밀리_치킨_10개;
import static cart.fixture.TestFixture.장바구니_밀리_피자_1개;
import static cart.fixture.TestFixture.주문_밀리_치킨_피자_3000원;
import static cart.fixture.TestFixture.주문_밀리_치킨_햄버거_3000원;
import static java.math.BigDecimal.valueOf;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.domain.coupon.repository.MemberCouponRepository;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.CouponException;
import cart.exception.OrderException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CouponService couponService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 주문을_등록한다() {
        given(cartItemRepository.findByIds(any()))
                .willReturn(List.of(장바구니_밀리_치킨_10개, 장바구니_밀리_피자_1개));
        given(orderRepository.save(any()))
                .willReturn(주문_밀리_치킨_피자_3000원);
        given(memberCouponRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리_쿠폰_10퍼센트));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        Long id = orderService.register(new OrderRequest(of(1L, 2L), 1L, 3000, valueOf(111000)), 밀리_인증_정보);

        verify(cartItemRepository, times(1)).deleteAllByIds(any());
        verify(memberCouponRepository, times(1)).delete(any());
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 쿠폰으로_주문할_때_최소_사용_가능_금액보다_작으면_예외가_발생한다() {
        given(cartItemRepository.findByIds(any()))
                .willReturn(List.of(장바구니_밀리_피자_1개));
        given(memberCouponRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리_쿠폰_10퍼센트));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        verify(orderRepository, never()).save(any());
        verify(cartItemRepository, never()).deleteAllByIds(any());
        verify(memberCouponRepository, never()).delete(any());
        assertThatThrownBy(() -> orderService.register(new OrderRequest(of(1L), 1L, 3000, valueOf(21000)), 밀리_인증_정보))
                .isInstanceOf(CouponException.class);
    }

    @Test
    void 쿠폰으로_주문할_때_만료기간이_지난_쿠폰이면_예외가_발생한다() {
        given(cartItemRepository.findByIds(any()))
                .willReturn(List.of(장바구니_밀리_피자_1개));
        given(memberCouponRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리_만료기간_지난_쿠폰_10퍼센트));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        verify(orderRepository, never()).save(any());
        verify(cartItemRepository, never()).deleteAllByIds(any());
        verify(memberCouponRepository, never()).delete(any());
        assertThatThrownBy(() -> orderService.register(new OrderRequest(of(1L), 1L, 3000, valueOf(21000)), 밀리_인증_정보))
                .isInstanceOf(CouponException.class);
    }

    @Test
    void 주문을_등록할_때_요청한_총_금액과_다르면_예외가_발생한다() {
        given(cartItemRepository.findByIds(any()))
                .willReturn(List.of(장바구니_밀리_치킨_10개, 장바구니_밀리_피자_1개));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        verify(orderRepository, never()).save(any());
        verify(cartItemRepository, never()).deleteAllByIds(any());
        verify(memberCouponRepository, never()).delete(any());
        assertThatThrownBy(
                () -> orderService.register(new OrderRequest(of(1L, 2L), -1L, 3000, valueOf(12000)), 밀리_인증_정보))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void 주문을_상세_조회한다() {
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(주문_밀리_치킨_피자_3000원));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        OrderDetailResponse response = orderService.findById(1L, 밀리_인증_정보);

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void 주문을_조회할_때_다른_사용자가_조회하면_예외가_발생한다() {
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(주문_밀리_치킨_피자_3000원));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(박스터));

        assertThatThrownBy(() -> orderService.findById(1L, 박스터_인증_정보))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void 사용자의_주문을_전체_조회한다() {
        given(orderRepository.findAllByMember(밀리))
                .willReturn(List.of(주문_밀리_치킨_피자_3000원, 주문_밀리_치킨_햄버거_3000원));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        List<OrderResponse> responses = orderService.findAll(밀리_인증_정보);

        assertThat(responses).map(OrderResponse::getId)
                .containsExactly(1L, 2L);
    }

    @Test
    void 주문을_할_때_쿠폰을_사용하지_않으면_쿠폰을_발급한경다() {
        given(cartItemRepository.findByIds(any()))
                .willReturn(List.of(장바구니_밀리_치킨_10개, 장바구니_밀리_피자_1개));
        given(orderRepository.save(any()))
                .willReturn(주문_밀리_치킨_피자_3000원);
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        Long id = orderService.register(new OrderRequest(of(1L, 2L), -1L, 3000, valueOf(123000)), 밀리_인증_정보);

        verify(couponService, times(1)).issueByOrderPrice(any(), any());
        verify(cartItemRepository, times(1)).deleteAllByIds(any());
        verify(memberCouponRepository, never()).delete(any());
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 주문을_할_때_쿠폰을_사용하면_쿠폰을_발급하지_않는다() {
        given(cartItemRepository.findByIds(any()))
                .willReturn(List.of(장바구니_밀리_치킨_10개, 장바구니_밀리_피자_1개));
        given(orderRepository.save(any()))
                .willReturn(주문_밀리_치킨_피자_3000원);
        given(memberCouponRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리_쿠폰_10퍼센트));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        Long id = orderService.register(new OrderRequest(of(1L, 2L), 1L, 3000, valueOf(111000)), 밀리_인증_정보);

        verify(couponService, never()).issueByOrderPrice(any(), any());
        verify(cartItemRepository, times(1)).deleteAllByIds(any());
        verify(memberCouponRepository, times(1)).delete(any());
        assertThat(id).isEqualTo(1L);
    }
}
