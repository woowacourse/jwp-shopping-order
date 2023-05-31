package cart.step2.coupontype.service;

import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import cart.step2.coupontype.presentation.dto.CouponTypeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CouponTypeServiceTest {

    @InjectMocks
    private CouponTypeService couponTypeService;

    @Mock
    private CouponTypeRepository couponTypeRepository;

    @DisplayName("List<Coupon>을 받아서 List<CouponTypeResponse>로 반환한다.")
    @Test
    void getCouponsType() {
        // given
        List<CouponType> couponTypes = List.of(
                new CouponType(1L, "할인쿠폰1", "1000원 할인 쿠폰", 1000),
                new CouponType(2L, "할인쿠폰2", "3000원 할인 쿠폰", 3000),
                new CouponType(3L, "할인쿠폰3", "5000원 할인 쿠폰", 5000),
                new CouponType(4L, "할인쿠폰4", "10000원 할인 쿠폰", 10000)
        );

        doReturn(couponTypes).when(couponTypeRepository).findAll();

        // when
        List<CouponTypeResponse> couponsType = couponTypeService.getCouponsType();

        // then
        assertAll(
                () -> assertThat(couponsType.get(0).getClass()).isEqualTo(CouponTypeResponse.class),
                () -> assertThat(couponsType).hasSize(4),
                () -> assertThat(couponsType).extracting(CouponTypeResponse::getId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(couponsType).extracting(CouponTypeResponse::getDiscountAmount)
                        .contains(1000, 3000, 5000, 10000),
                () -> assertThat(couponsType).extracting(CouponTypeResponse::getName)
                        .contains("할인쿠폰1", "할인쿠폰2", "할인쿠폰3", "할인쿠폰4"),
                () -> assertThat(couponsType).extracting(CouponTypeResponse::getDescription)
                        .contains("1000원 할인 쿠폰", "3000원 할인 쿠폰", "5000원 할인 쿠폰", "10000원 할인 쿠폰")
        );
    }

}
