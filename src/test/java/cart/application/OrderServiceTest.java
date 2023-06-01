package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.OrderRequestDto;
import cart.exception.CartItemNotFoundException;
import cart.exception.CouponNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private CouponDao couponDao;

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
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, new Product("name1", 100, "image1"), null),
                        new CartItem(2L, 2, new Product("name2", 200, "image2"), null)
                ));

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1L), 1000, null);

        //when, then
        assertDoesNotThrow(() -> orderService.order(member, orderRequestDto));
    }
}
