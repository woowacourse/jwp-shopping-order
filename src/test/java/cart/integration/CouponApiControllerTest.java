package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.CouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.vo.Amount;
import cart.dto.CouponDiscountRequest;
import cart.dto.CouponDiscountResponse;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CouponApiControllerTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CouponDao couponDao;

    private Long productId1;
    private Long productId2;
    private Member member;
    private Coupon coupon1;
    private Coupon coupon2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        productId1 = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberDao.getMemberById(1L);

        coupon1 = couponDao.save(new Coupon("3000원 할인 쿠폰", Amount.of(3_000), Amount.of(10_000), false), member.getId());
        coupon2 = couponDao.save(new Coupon("5000원 할인 쿠폰", Amount.of(5_000), Amount.of(20_000), true), member.getId());
    }

    @Test
    @DisplayName("멤버에게 쿠폰을 등록한다.")
    void testRegisterCouponToMember() {
        //given
        //when
        final ExtractableResponse<Response> response = registerCouponToMember(coupon1.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> registerCouponToMember(final Long couponId) {
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .post("/coupons/" + couponId)
            .then()
            .log().all()
            .extract();
        return response;
    }

    @Test
    @DisplayName("쿠폰을 모두 보여준다.")
    void testFindAllMemberCoupons() {
        //given
        registerCouponToMember(coupon1.getId());
        registerCouponToMember(coupon2.getId());

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/coupons")
            .then()
            .log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("쿠폰 할인 금액을 조회한다.")
    void testCalculateDiscount() {
        //given
        final CouponDiscountRequest request = new CouponDiscountRequest(coupon1.getId(), 15_000);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .body(request)
            .when()
            .get("/coupons/discount")
            .then()
            .log().all()
            .extract();
        final CouponDiscountResponse couponDiscountResponse = response.as(CouponDiscountResponse.class);

        //then
        assertThat(couponDiscountResponse.getDiscountedProductAmount()).isEqualTo(12_000);
    }
}
