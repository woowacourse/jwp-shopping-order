package cart.dao;

import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.CouponFixture.COUPON_2_NOT_NULL_RATE;
import static org.assertj.core.api.Assertions.assertThat;

import anotation.RepositoryTest;
import cart.dto.CouponDto;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class CouponDaoTest {

    @Autowired
    private CouponDao couponDao;

    @Test
    @DisplayName("CouponDao 를 이용해 Coupon 을 저장한다.")
    void insert() {
        CouponDto couponDto = new CouponDto(1L, "홍실 할인", 2d, 0);

        Long insertCouponId = couponDao.insert(couponDto);
        CouponDto couponDtoAfterSave = couponDao.findById(insertCouponId).orElseThrow(NoSuchElementException::new);

        assertThat(couponDtoAfterSave).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(couponDto);
    }

    @Test
    @DisplayName("Coupon Dto 를 조회하는 기능 테스트")
    void findByIdTest() {
        CouponDto couponDto = couponDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(couponDto)
                .extracting(CouponDto::getId, CouponDto::getName, CouponDto::getDiscountRate, CouponDto::getDiscountPrice)
                .containsExactly(1L, "정액 할인 쿠폰", 0.0, 5000);
    }

    @Test
    @DisplayName("찾는 Coupon Dto 가 없는 경우 빈 Optional 을 반환한다.")
    void findById_returnEmpty() {
        Optional<CouponDto> couponDto = couponDao.findById(3L);

        assertThat(couponDto).isNotPresent();
    }

    @Test
    @DisplayName("쿠폰 전체 조회")
    void findAll() {
        List<CouponDto> couponDtos = couponDao.findAll();
        assertThat(couponDtos).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(COUPON_1_NOT_NULL_PRICE, COUPON_2_NOT_NULL_RATE));
    }

}