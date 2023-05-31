package cart.integration.steps;

import static cart.integration.steps.CommonStep.헤더_ID_값_파싱;
import static io.restassured.RestAssured.given;

import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemStep {
    public static ExtractableResponse<Response> 장바구니_상품_추가_요청(Member 멤버, Long 장바구니_상품_ID) {
        CartItemRequest 장바구니_상품_추가_요청값 = new CartItemRequest(장바구니_상품_ID);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(멤버.getEmail(), 멤버.getPassword())
                .body(장바구니_상품_추가_요청값)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_상품_조회_요청(Member 멤버) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(멤버.getEmail(), 멤버.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_상품_수정_요청(Member 멤버, Long 장바구니_상품_ID, int 장바구니_상품_수량) {
        CartItemQuantityUpdateRequest 장바구니_상품_수정_요청값 = new CartItemQuantityUpdateRequest(장바구니_상품_수량);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(멤버.getEmail(), 멤버.getPassword())
                .when()
                .body(장바구니_상품_수정_요청값)
                .patch("/cart-items/{cartItemId}", 장바구니_상품_ID)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_상품_삭제_요청(Member 멤버, Long 장바구니_상품_ID) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(멤버.getEmail(), 멤버.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", 장바구니_상품_ID)
                .then()
                .log().all()
                .extract();
    }

    public static Long 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(Member 멤버, Long 상품_ID) {
        var 응답 = 장바구니_상품_추가_요청(멤버, 상품_ID);
        return 헤더_ID_값_파싱(응답);
    }
}
