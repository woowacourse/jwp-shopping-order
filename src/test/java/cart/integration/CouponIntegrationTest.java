package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.application.coupon.dto.CouponRequest;
import cart.application.coupon.dto.CouponResponse;
import cart.domain.coupon.type.CouponType;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;

public class CouponIntegrationTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	private CouponRequest couponRequest;
	private CouponRequest couponRequest2;

	@BeforeEach
	void setUp() {
		super.setUp();

		couponRequest = new CouponRequest("old Coupon", CouponType.PERCENTAGE.name(), BigDecimal.TEN, 50);
		couponRequest2 = new CouponRequest("New Coupon", CouponType.PERCENTAGE.name(), BigDecimal.valueOf(11), 30);
	}

	@Test
	@DisplayName("멤버의 쿠폰 목록을 조회한다.")
	void showMemberCouponsTest() {
		//given
		final Member member = memberRepository.findById(2L).get();

		//when
		final List<CouponResponse> couponResponses = given().log().all()
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.get("/coupons")
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath()
			.getList(".", CouponResponse.class);

		//then
		assertThat(couponResponses).hasSize(2);
		assertThat(couponResponses.get(0).getName()).isEqualTo("배송비 할인");
		assertThat(couponResponses.get(1).getName()).isEqualTo("신규 가입 할인");
	}

	@Test
	@DisplayName("쿠폰을 생성한다.")
	int createCouponTest() {
		final String location = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(couponRequest)
			.when()
			.post("/coupons")
			.then()
			.log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract().header("Location");

		Path p = Paths.get(location);
		return Integer.parseInt(p.getFileName().toString());
	}

	@Test
	@DisplayName("멤버에게 쿠폰을 지급한다.")
	void addCouponTest() {
		//given
		final int couponId = createCouponTest();
		final Member member = memberRepository.findById(2L).get();

		//when & then
		given().log().all()
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.post("/coupons/{id}", couponId)
			.then()
			.log().all()
			.statusCode(HttpStatus.CREATED.value());

	}

	@Test
	@DisplayName("쿠폰의 내용을 변경한다.")
	void updateCouponTest() {
		//given
		final int couponId = createCouponTest();

		//when & then
		given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(couponRequest2)
			.when()
			.put("/coupons/{id}", couponId)
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("기존재 쿠폰을 추가 발행한다.")
	void generateExtraCoupons() {
		//given
		final int couponId = createCouponTest();

		//when
		given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(couponRequest2)
			.when()
			.patch("/coupons/{id}", couponId)
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("쿠폰을 모든 DB에서 삭제한다")
	void deleteCouponTest() {
		//given
		final int couponId = createCouponTest();

		//when
		given().log().all()
			.when()
			.delete("/coupons/{id}", couponId)
			.then()
			.log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
