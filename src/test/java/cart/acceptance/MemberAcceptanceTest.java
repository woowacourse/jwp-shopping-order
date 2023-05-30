package cart.acceptance;

import static cart.fixtures.MemberFixtures.Dooly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dto.MemberCashChargeRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("사용자의 금액 충전 시")
    class chargeCash {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // given
            int chargeCash = 10000;
            MemberCashChargeRequest request = new MemberCashChargeRequest(chargeCash);

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/members/cash")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";
            int chargeCash = 10000;
            MemberCashChargeRequest request = new MemberCashChargeRequest(chargeCash);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/members/cash")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Nested
        @DisplayName("인증된 사용자와 충전할 금액을 담아서 요청 시")
        class requestWithAuthMemberAndCashToCharge {

            @Test
            @DisplayName("충전할 금액이 0 이하이면 예외가 발생한다.")
            void throws_when_cashToCharge_under_zero() {
                // given
                int cashToCharge = -10000;
                MemberCashChargeRequest request = new MemberCashChargeRequest(cashToCharge);

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .post("/members/cash")
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("충전할 금액은 1원 이상이어야합니다.")
                );
            }
        }
    }
}
