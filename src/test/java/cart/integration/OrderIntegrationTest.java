package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.ProductInOrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberService memberService;

    @Test
    void 주문_상세_조회_테스트() {
        final Member member = memberService.findMemberById(1L);

        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .header(HttpHeaders.ORIGIN, "http://www.example.com") // check CORS
                .when()
                .get("/orders/{id}", 1L)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("priceBeforeDiscount")).isEqualTo(50_000);
        assertThat(response.jsonPath().getInt("priceAfterDiscount")).isEqualTo(45_000);
        final List<ProductInOrderResponse> productsResponse = new ArrayList<>(
                response.jsonPath().getList("products", ProductInOrderResponse.class)
        );
        assertThat(productsResponse).hasSize(2);
        assertThat(productsResponse).extracting("name").contains("치킨", "샐러드");
    }
}
