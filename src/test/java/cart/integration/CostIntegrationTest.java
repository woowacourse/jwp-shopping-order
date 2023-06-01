package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.CostResponse;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class CostIntegrationTest extends IntegrationTest{

    @Autowired
    private MemberDao memberDao;
    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("피자", 10_000, "http://example.com/pizza.jpg", 50));

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    @Test
    @DisplayName("장바구니 상품들의 가격들에 대한 정보를 받아온다")
    void getCosts(){
        int totalItemDiscountAmount = 12_600;
        int totalMemberDiscountAmount = 1_000;

        int totalItemPrice = 43_000;
        int discountedTotalItemPrice = 29_400;
        int shippingFee = 3_000;
        int totalPrice = 32_400;

        CostResponse costResponse = requestGetCosts(member2);
        assertThat(costResponse.getTotalItemPrice()).isEqualTo(totalItemPrice);
        assertThat(costResponse.getShippingFee()).isEqualTo(shippingFee);
        assertThat(costResponse.getTotalMemberDiscountAmount()).isEqualTo(totalMemberDiscountAmount);
        assertThat(costResponse.getDiscountedTotalItemPrice()).isEqualTo(discountedTotalItemPrice);

        assertThat(costResponse.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(costResponse.getTotalItemDiscountAmount()).isEqualTo(totalItemDiscountAmount);

    }

    private CostResponse requestGetCosts(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/costs")
                .then()
                .log().all()
                .extract()
                .jsonPath()
                .getObject(".", CostResponse.class);
    }

    private ExtractableResponse<Response> requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }
}
