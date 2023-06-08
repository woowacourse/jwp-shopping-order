package cart.step2.coupontype.persist;

import cart.step2.coupontype.domain.CouponType;
import cart.step2.coupontype.domain.CouponTypeEntity;
import cart.step2.coupontype.exception.CouponTypeNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CouponTypeRepositoryMapperTest {

    @InjectMocks
    private CouponTypeRepositoryMapper couponTypeRepositoryMapper;

    @Mock
    private CouponTypeDao couponTypeDao;

    @DisplayName("Optional<CouponTypeEntity>를 단건 조회해서 CouponType으로 변환해서 반환한다.")
    @Test
    void findById() {
        // given
        Optional<CouponTypeEntity> couponTypeEntityOptional = Optional.of(new CouponTypeEntity(1L, "할인쿠폰1", "1000원 할인 쿠폰", 1000));
        doReturn(couponTypeEntityOptional).when(couponTypeDao).findById(1L);

        // when
        CouponType couponType = couponTypeRepositoryMapper.findById(1L);

        // then
        assertAll(
                () -> assertThat(couponType.getClass()).isEqualTo(CouponType.class),
                () -> assertThat(couponType.getId()).isEqualTo(1),
                () -> assertThat(couponType.getDiscountAmount()).isEqualTo(1000),
                () -> assertThat(couponType.getName()).isEqualTo("할인쿠폰1"),
                () -> assertThat(couponType.getDescription()).isEqualTo("1000원 할인 쿠폰")
        );
    }

    @DisplayName("Optional<CouponTypeEntity>를 단건 조회할 때 예외 발생")
    @Test
    void findByIdFailed() {
        // given
        Optional<CouponTypeEntity> couponTypeEntityOptional = Optional.empty();
        doReturn(couponTypeEntityOptional).when(couponTypeDao).findById(1L);

        // when, then
        assertThrows(CouponTypeNotFoundException.class, () -> couponTypeRepositoryMapper.findById(1L));
    }

    @DisplayName("모든 CouponTypeEntity을 조회해서 List<CouponType>으로 변환해서 반환한다.")
    @Test
    void findAll() {
        // given
        List<CouponTypeEntity> couponTypeEntities = List.of(
                new CouponTypeEntity(1L, "할인쿠폰1", "1000원 할인 쿠폰", 1000),
                new CouponTypeEntity(2L, "할인쿠폰2", "3000원 할인 쿠폰", 3000),
                new CouponTypeEntity(3L, "할인쿠폰3", "5000원 할인 쿠폰", 5000),
                new CouponTypeEntity(4L, "할인쿠폰4", "10000원 할인 쿠폰", 10000)
        );

        doReturn(couponTypeEntities).when(couponTypeDao).findAllOrderByDiscountAmount();

        // when
        List<CouponType> couponTypes = couponTypeRepositoryMapper.findAll();

        // then
        assertAll(
                () -> assertThat(couponTypes.get(0)).extracting(CouponType::getClass)
                        .isEqualTo(CouponType.class),
                () -> assertThat(couponTypes).hasSize(4),
                () -> assertThat(couponTypes).extracting(CouponType::getId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(couponTypes).extracting(CouponType::getDiscountAmount)
                        .contains(1000, 3000, 5000, 10000),
                () -> assertThat(couponTypes).extracting(CouponType::getName)
                        .contains("할인쿠폰1", "할인쿠폰2", "할인쿠폰3", "할인쿠폰4"),
                () -> assertThat(couponTypes).extracting(CouponType::getDescription)
                        .contains("1000원 할인 쿠폰", "3000원 할인 쿠폰", "5000원 할인 쿠폰", "10000원 할인 쿠폰")
        );
    }

}
