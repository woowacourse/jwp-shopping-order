package cart.acceptance;

import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;

@SuppressWarnings("NonAsciiCharacters")
public class OrderAcceptanceTest extends AcceptanceTest {

    /**
     * given 장바구니의 상품들의 아이디와 가격 쿠폰 아이디를 받아서
     * when 주문을 요청하면
     * then 성공한다
     */
    @Test
    void 주문_한다() {
        // given
        final OrderRequest request = new OrderRequest(List.of(1L, 2L), 15000, 1L);

        // when
        final ExtractableResponse<Response> response = 주문_한다(request);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/orders/")
        );
    }

    private ExtractableResponse<Response> 주문_한다(final OrderRequest request) {
        return givenBasic()
                .when()
                .body(request)
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }
}
