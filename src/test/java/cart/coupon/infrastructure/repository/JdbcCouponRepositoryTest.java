package cart.coupon.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.FixDiscountPolicy;
import cart.coupon.domain.GeneralCouponType;
import cart.coupon.domain.RateDiscountPolicy;
import cart.coupon.domain.SpecificCouponType;
import cart.coupon.domain.TargetType;
import cart.coupon.infrastructure.dao.CouponDao;
import cart.coupon.infrastructure.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
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
@DisplayName("JdbcCouponRepository 은(는)")
class JdbcCouponRepositoryTest {

    @Mock
    private CouponDao couponDao;

    @InjectMocks
    private JdbcCouponRepository jdbcCouponRepository;

    @Test
    void 쿠폰을_저장한다() {
        // given
        given(couponDao.save(any()))
                .willReturn(1L);
        Coupon 코코닥_불쌍해서_주는_쿠폰 = new Coupon(
                "코코닥 불쌍해서 주는 쿠폰",
                new FixDiscountPolicy(1000),
                new SpecificCouponType(1L),
                2L);

        // when
        Long id = jdbcCouponRepository.save(코코닥_불쌍해서_주는_쿠폰);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 쿠폰을_조회한다() {
        // given
        CouponEntity couponEntity = new CouponEntity(
                1L,
                "말랑이 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                null,
                50);
        given(couponDao.findById(1L))
                .willReturn(Optional.of(couponEntity));

        // when
        Coupon coupon = jdbcCouponRepository.findById(1L);

        // then
        Coupon expected = new Coupon(
                1L,
                "말랑이 멋진쿠폰",
                new RateDiscountPolicy(50),
                new SpecificCouponType(1L),
                1L);
        assertThat(coupon).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 특정_회원의_쿠폰을_조회한다() {
        // given
        CouponEntity couponEntity1 = new CouponEntity(
                1L,
                "말랑이 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                null,
                50);
        CouponEntity couponEntity2 = new CouponEntity(
                2L,
                "코코닥 멋진쿠폰",
                1L,
                DiscountType.FIX,
                TargetType.SPECIFIC,
                1L,
                5000000);
        given(couponDao.findAllByMemberId(1L))
                .willReturn(List.of(couponEntity1, couponEntity2));

        // when
        List<Coupon> allByMemberId = jdbcCouponRepository.findAllByMemberId(1L);

        // then
        Coupon expected1 = new Coupon(
                1L,
                "말랑이 멋진쿠폰",
                new RateDiscountPolicy(50),
                new GeneralCouponType(),
                1L);
        Coupon expected2 = new Coupon(
                2L,
                "코코닥 멋진쿠폰",
                new FixDiscountPolicy(5000000),
                new SpecificCouponType(1L),
                1L);
        List<Coupon> expected = List.of(expected1, expected2);
        assertThat(allByMemberId).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void ID_리스트로_모든_쿠폰을_조회한다() {
        // given
        CouponEntity couponEntity1 = new CouponEntity(
                1L,
                "말랑이 멋진쿠폰",
                1L,
                DiscountType.RATE,
                TargetType.ALL,
                null,
                50);
        CouponEntity couponEntity2 = new CouponEntity(
                2L,
                "코코닥 멋진쿠폰",
                1L,
                DiscountType.FIX,
                TargetType.SPECIFIC,
                1L,
                5000000);
        given(couponDao.findAllByIds(any()))
                .willReturn(List.of(couponEntity1, couponEntity2));

        // when
        List<Coupon> actual = jdbcCouponRepository.findAllByIds(List.of(1L, 2L));

        // then
        Coupon expected1 = new Coupon(
                1L,
                "말랑이 멋진쿠폰",
                new RateDiscountPolicy(50),
                new GeneralCouponType(),
                1L);
        Coupon expected2 = new Coupon(
                2L,
                "코코닥 멋진쿠폰",
                new FixDiscountPolicy(5000000),
                new SpecificCouponType(1L),
                1L);
        List<Coupon> expected = List.of(expected1, expected2);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
