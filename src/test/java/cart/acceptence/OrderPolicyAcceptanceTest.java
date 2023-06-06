package cart.acceptence;

import cart.dto.response.OrderPolicyResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static cart.acceptence.steps.OrderPolicySteps.할인_정책_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("할인 정책 관리 기능")
public class OrderPolicyAcceptanceTest extends AcceptanceTest {

    @Nested
    class 할인_정책을_조회할_때 {

        @Test
        void 정상_요청이면_성공적으로_조회한다() {
            // when
            ExtractableResponse<Response> 할인_정책_조회_결과 = 할인_정책_조회_요청();
            OrderPolicyResponse 할인_정책 = 할인_정책_조회_결과.jsonPath().getObject(".", OrderPolicyResponse.class);

            // then
            assertThat(할인_정책_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(할인_정책).usingRecursiveComparison()
                    .isEqualTo(new OrderPolicyResponse(
                            30000L,
                            3000L,
                            10L
                    ));
        }
    }
}
