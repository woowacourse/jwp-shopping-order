package cart.integration.steps;

import static io.restassured.RestAssured.given;

import cart.domain.Member;
import cart.dto.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderStep {
    public static ExtractableResponse<Response> 주문_추가_요청(Member 멤버, List<Long> 장바구니_상품_ID들, int 사용한_포인트,
                                                         ObjectMapper objectMapper) throws JsonProcessingException {
        OrderRequest 요청 = new OrderRequest(장바구니_상품_ID들, 사용한_포인트);
        String 요청_바디 = objectMapper.writeValueAsString(요청);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(멤버.getEmail(), 멤버.getPassword())
                .body(요청_바디)
                .when().post("/orders")
                .then()
                .log().all()
                .extract();
    }
}
