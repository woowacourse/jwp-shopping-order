package cart.coupon.domain;

import static cart.coupon.exception.CouponExceptionType.APPLY_MULTIPLE_TO_PRODUCT;
import static cart.coupon.exception.CouponExceptionType.EXIST_UNUSED_COUPON;
import static cart.coupon.exception.CouponExceptionType.NO_AUTHORITY_USE_COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.cartitem.domain.CartItem;
import cart.common.execption.BaseExceptionType;
import cart.coupon.exception.CouponException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponValidator (은)는")
class CouponValidatorTest {

    @Test
    void 쿠폰의_주인이_맞는지_검증한다() {
        // given
        Coupon 말랑이_멋있어서_주는_쿠폰 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰",
                new FixDiscountPolicy(99999999),
                new GeneralCouponType(),
                1L);
        CouponValidator couponValidator = new CouponValidator();

        // when
        BaseExceptionType baseExceptionType = assertThrows(CouponException.class, () ->
                couponValidator.validate(2L, null, List.of(말랑이_멋있어서_주는_쿠폰))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(NO_AUTHORITY_USE_COUPON);
    }

    @Test
    void 한_상품에_적용가능한_쿠폰_개수가_2개_이상일_경우_예외() {
        // given
        CouponValidator couponValidator = new CouponValidator();
        Coupon 말랑이_멋있어서_주는_쿠폰1 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰1",
                new FixDiscountPolicy(99999999),
                new SpecificCouponType(1L),
                1L);
        Coupon 말랑이_멋있어서_주는_쿠폰2 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰2",
                new RateDiscountPolicy(50),
                new GeneralCouponType(),
                1L);
        CartItem 코코닭 = new CartItem(1L, 100, 1L, "코코닭", "닭집.com", 99, 1L);
        CartItem 코코오리 = new CartItem(2L, 10, 2L, "코코오리", "오리집.com", 19, 1L);

        // when
        BaseExceptionType baseExceptionType = assertThrows(CouponException.class, () ->
                couponValidator.validate(
                        1L,
                        List.of(코코닭, 코코오리),
                        List.of(말랑이_멋있어서_주는_쿠폰1, 말랑이_멋있어서_주는_쿠폰2))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(APPLY_MULTIPLE_TO_PRODUCT);
    }

    @Test
    void 하나도_적용되지_않는_쿠폰이_존재할_경우_예외() {
        // given
        CouponValidator couponValidator = new CouponValidator();
        Coupon 말랑이_멋있어서_주는_쿠폰1 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰1",
                new FixDiscountPolicy(99999999),
                new SpecificCouponType(1L),
                1L);
        Coupon 말랑이_멋있어서_주는_쿠폰2 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰2",
                new RateDiscountPolicy(50),
                new SpecificCouponType(3L),
                1L);
        CartItem 코코닭 = new CartItem(1L, 100, 1L, "코코닭", "닭집.com", 99, 1L);
        CartItem 코코오리 = new CartItem(2L, 10, 2L, "코코오리", "오리집.com", 19, 1L);

        // when
        BaseExceptionType baseExceptionType = assertThrows(CouponException.class, () ->
                couponValidator.validate(
                        1L,
                        List.of(코코닭, 코코오리),
                        List.of(말랑이_멋있어서_주는_쿠폰1, 말랑이_멋있어서_주는_쿠폰2))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(EXIST_UNUSED_COUPON);
    }

    @Test
    void 별_문제_없으면_통과() {
        // given
        CouponValidator couponValidator = new CouponValidator();
        Coupon 말랑이_멋있어서_주는_쿠폰1 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰1",
                new FixDiscountPolicy(99999999),
                new SpecificCouponType(1L),
                1L);
        Coupon 말랑이_멋있어서_주는_쿠폰2 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰2",
                new RateDiscountPolicy(50),
                new SpecificCouponType(2L),
                1L);
        CartItem 코코닭 = new CartItem(1L, 100, 1L, "코코닭", "닭집.com", 99, 1L);
        CartItem 코코오리 = new CartItem(2L, 10, 2L, "코코오리", "오리집.com", 19, 1L);

        // when & then
        assertDoesNotThrow(() ->
                couponValidator.validate(1L, List.of(코코닭, 코코오리), List.of(말랑이_멋있어서_주는_쿠폰1, 말랑이_멋있어서_주는_쿠폰2))
        );
    }
}