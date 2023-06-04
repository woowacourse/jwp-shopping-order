package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import cart.application.coupon.dto.CouponResponse;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;

public class CouponIntegrationTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("유저의 쿠폰 목록을 조회한다.")
	void getOrderListTest() {
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
		assertThat(couponResponses.get(0).getName()).isEqualTo("배송비 3000원 할인");
		assertThat(couponResponses.get(1).getName()).isEqualTo("신규 가입 할인");
	}

}
