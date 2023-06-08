package cart.order.domain.service;

import static cart.coupon.exception.CouponExceptionType.APPLY_MULTIPLE_TO_PRODUCT;
import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.common.execption.BaseExceptionType;
import cart.coupon.domain.AllProductsCouponStrategy;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.RateDiscountPolicy;
import cart.coupon.exception.CouponException;
import cart.member.domain.Member;
import cart.order.domain.OrderRepository;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderPlaceService 은(는)")
class OrderPlaceServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderPlaceService orderPlaceService;

    @Test
    void 장바구니_상품을_주문한다() {
        // given
        given(orderRepository.save(any())).willReturn(1L);
        Product product = new Product(1L, "말랑", 1000, "image");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem = new CartItem(2L, 10, product, member);
        given(productRepository.findById(1L)).willReturn(product);

        // when
        Long id = orderPlaceService.placeOrder(member.getId(), of(cartItem), Collections.emptyList());

        // then
        assertThat(id).isEqualTo(1L);
        then(cartItemRepository).should(times(1))
                .deleteById(any());
    }

    @Test
    void 상품_정보가_변경되었으면_주문할_수_없다() {
        // given
        Product product = new Product(1L, "말랑", 1000, "image");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem = new CartItem(2L, 10, product, member);
        Product updated = new Product(1L, "말랑", 3000, "image");
        given(productRepository.findById(1L)).willReturn(updated);

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderPlaceService.placeOrder(member.getId(), of(cartItem), Collections.emptyList())
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(MISMATCH_PRODUCT);
    }

    @Test
    void 쿠폰_관련_오류가_발생하면_주문할_수_없다() {
        // given
        Product product = new Product(1L, "말랑", 1000, "image");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem = new CartItem(2L, 10, product, member);
        Coupon 쿠폰1 = new Coupon(1L, "쿠폰1",
                new RateDiscountPolicy(50),
                new AllProductsCouponStrategy(),
                1L
        );
        Coupon 쿠폰2 = new Coupon(2L, "쿠폰2",
                new RateDiscountPolicy(50),
                new AllProductsCouponStrategy(),
                1L
        );

        // when
        BaseExceptionType baseExceptionType = assertThrows(CouponException.class, () ->
                orderPlaceService.placeOrder(member.getId(), of(cartItem), List.of(쿠폰1, 쿠폰2))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(APPLY_MULTIPLE_TO_PRODUCT);
    }

    @Test
    void 주문_이후_장바구니를_비운다() {
        // given
        Product product1 = new Product(1L, "말랑", 1000, "image");
        Product product2 = new Product(2L, "코코닥", 2000, "image2");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem1 = new CartItem(1L, 10, product1, member);
        CartItem cartItem2 = new CartItem(2L, 20, product2, member);
        given(productRepository.findById(1L)).willReturn(product1);
        given(productRepository.findById(2L)).willReturn(product2);
        given(orderRepository.save(any())).willReturn(1L);

        // when
        Long place = orderPlaceService.placeOrder(member.getId(), List.of(cartItem1, cartItem2), Collections.emptyList());

        // then
        assertThat(place).isEqualTo(1L);
        then(cartItemRepository).should(times(2))
                .deleteById(any());
    }
}
