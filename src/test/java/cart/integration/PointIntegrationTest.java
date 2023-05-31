package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PointIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("멤버가 가진 포인트를 조회한다.")
    void findPointOfMember() {
        Member member = memberDao.findByEmail("yis092521@gmail.com");
        final ExtractableResponse<Response> response = findPointOfMember(member);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getInt("point")).isEqualTo(3000)
        );

    }

    private static ExtractableResponse<Response> findPointOfMember(final Member member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .extract();
    }

    @Test
    @DisplayName("결제 금액에 따른 적립될 포인트를 계산한다.")
    void calculatePointByPayment() {
        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("totalPrice", 3000)
                .when()
                .get("/saving-point")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getInt("savingPoint")).isEqualTo(300)
        );
    }
}
