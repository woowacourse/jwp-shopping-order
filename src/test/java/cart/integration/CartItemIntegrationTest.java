package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemAddRequest;
import cart.dto.CartItemResponse;
import cart.dto.CartItemUpdateRequest;
import cart.dto.CartItemUpdateResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CartItemIntegrationTest extends IntegrationTest {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ProductDao productDao;

	private Long chickenId;
	private Long saladId;
	private Member member;

	@BeforeEach
	void setUp() {
		super.setUp();
		chickenId = Product.from(productDao.getProductById(1L)).getId();
		saladId = Product.from(productDao.getProductById(2L)).getId();
		member = memberDao.getMemberById(1L);
	}

	@Test
	void 장바구니에_상품을_등록한다() {
		// given
		final Member member = new Member(this.member.getId(), this.member.getEmail(), this.member.getPassword());
		final CartItemAddRequest cartItemAddRequest = new CartItemAddRequest(chickenId);

		// when
		final ExtractableResponse<Response> response = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(cartItemAddRequest)
			.when().post("/cart-items")
			.then().log().all()
			.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	void 장바구니에_상품_등록시_사용자_정보가_잘못되면_실패한다() {
		// given
		Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "bad");
		final CartItemAddRequest cartItemAddRequest = new CartItemAddRequest(chickenId);

		// when
		final ExtractableResponse<Response> response = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(illegalMember.getEmail(), illegalMember.getPassword())
			.body(cartItemAddRequest)
			.when().post("/cart-items")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	void 사용자가_장바구니에_담은_상품들을_조회한다() {
		// given
		final CartItemAddRequest cartItemAddRequest1 = new CartItemAddRequest(chickenId);
		final CartItemAddRequest cartItemAddRequest2 = new CartItemAddRequest(saladId);

		// when
		given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(cartItemAddRequest1)
			.when().post("/cart-items")
			.then().log().all();

		given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(cartItemAddRequest2)
			.when().post("/cart-items")
			.then().log().all();

		final ExtractableResponse<Response> response = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.get("/cart-items")
			.then()
			.log().all()
			.extract();

		final List<Long> cartItemIds = response.jsonPath()
			.getList(".", CartItemResponse.class).stream()
			.map(CartItemResponse::getId)
			.collect(Collectors.toList());

		// then
		assertThat(cartItemIds).containsAll(Arrays.asList(chickenId, saladId));
	}

	@Test
	void 장바구니에_담긴_상품의_수량을_변경한다() {
		// given
		final CartItemUpdateRequest updateRequest = new CartItemUpdateRequest(8, true);

		// when
		final ExtractableResponse<Response> response = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(updateRequest)
			.when().patch("/cart-items/" + chickenId)
			.then().log().all()
			.extract();

		final CartItemUpdateResponse updateResponse = response.as(CartItemUpdateResponse.class);

		// then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(updateResponse.getQuantity()).isEqualTo(8),
			() -> assertThat(updateResponse.isChecked()).isTrue()
		);
	}

	@Test
	void 장바구니에_담긴_상품의_체크여부를_변경한다() {
		// given
		final CartItemUpdateRequest updateRequest = new CartItemUpdateRequest(2, false);

		// when
		final ExtractableResponse<Response> response = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(updateRequest)
			.when().patch("/cart-items/" + chickenId)
			.then().log().all()
			.extract();

		final CartItemUpdateResponse updateResponse = response.as(CartItemUpdateResponse.class);

		// then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(updateResponse.getQuantity()).isEqualTo(2),
			() -> assertThat(updateResponse.isChecked()).isEqualTo(false)
		);
	}

	@Test
	void 장바구니에_담긴_상품들은_삭제한다() {
		// given
		final CartItemAddRequest cartItemAddRequest = new CartItemAddRequest(chickenId);

		// when
		// delete a cart-item
		final ExtractableResponse<Response> deleteResponse = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
			.when()
			.delete("/cart-items/{cartItemId}", chickenId)
			.then()
			.log().all()
			.extract();

		// find cart items
		final ExtractableResponse<Response> response = given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.get("/cart-items")
			.then()
			.log().all()
			.extract();

		Optional<CartItemResponse> resultResponse = response.jsonPath()
			.getList(".", CartItemResponse.class).stream()
			.filter(cartItemResponse -> cartItemResponse.getId().equals(chickenId))
			.findFirst();

		// then
		assertAll(
			() -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
			() -> assertThat(resultResponse.isPresent()).isFalse()
		);
	}
}
