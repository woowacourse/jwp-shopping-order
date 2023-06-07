package cart.service.coupon;

import cart.dto.coupon.CouponCreateRequest;
import cart.dto.coupon.CouponResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/data.sql")
class CouponServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CouponService couponService;

    @BeforeEach
    void init() {
        RestAssured.port = this.port;
    }

    @DisplayName("쿠폰을 전체 조회한다.")
    @Test
    void find_all_coupons() {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 10);
        couponService.createCoupon(req);

        // when
        List<CouponResponse> result = couponService.findAllCoupons();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0).getCouponName()).isEqualTo(req.getName())
        );
    }

    @DisplayName("쿠폰을 id값 기준으로 찾는다.")
    @Test
    void find_coupon_by_id() {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 10);
        long couponId = couponService.createCoupon(req);

        // when
        CouponResponse result = couponService.findById(couponId);

        // then
        assertAll(
                () -> assertThat(result.getCouponId()).isEqualTo(couponId),
                () -> assertThat(result.getCouponName()).isEqualTo(req.getName())
        );
    }

    @DisplayName("쿠폰을 저장한다.")
    @Test
    void save_coupon() {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 10);
        List<CouponResponse> allCoupons = couponService.findAllCoupons();

        // when
        long couponId = couponService.createCoupon(req);

        // then
        assertAll(
                () -> assertThat(allCoupons.size()).isEqualTo(0),
                () -> assertThat(couponId).isEqualTo(1)
        );
    }

    @DisplayName("쿠폰을 id값 기준으로 삭제한다.")
    @Test
    void delete_coupon_by_id() {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 10);
        long couponId = couponService.createCoupon(req);

        // when
        couponService.deleteCoupon(couponId);

        // then
        List<CouponResponse> allCoupons = couponService.findAllCoupons();
        assertThat(allCoupons.size()).isEqualTo(0);
    }
}
