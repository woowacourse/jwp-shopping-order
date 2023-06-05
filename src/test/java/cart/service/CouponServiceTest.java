package cart.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.dto.CouponSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Test
    void 새로운_쿠폰을_생성한다() {
        // given
        final CouponSaveRequest request = new CouponSaveRequest("1000원이상 결제시 1원쿠폰", "PRICE", 1L, 1000L);

        // when then
        assertDoesNotThrow(() -> couponService.save(request));
    }
}
