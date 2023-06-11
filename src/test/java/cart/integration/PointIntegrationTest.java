package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.dto.response.PointResponse;
import cart.dto.response.SavingPointResponse;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class PointIntegrationTest extends IntegrationTest {
    @Autowired
    MemberRepository memberRepository;

    private Member 사용자;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        사용자 = memberRepository.findById(1L);
    }

    @DisplayName("사용자가 가진 포인트를 조회한다")
    @Test
    void pointDetailByMember() {
        // when
        PointResponse 포인트_응답_객체 = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .when()
                .get("/points")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", PointResponse.class);

        // then
        assertAll(
                () -> assertThat(포인트_응답_객체.getPoint()).isEqualTo(1000)
        );
    }

    @DisplayName("적립될 포인트를 조회한다")
    @Test
    void pointSavedTotalPrice() {
        // given
        final int totalPrice = 1000;

        // when
        SavingPointResponse 적립된_포인트_응답_객체 = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/saving-point?totalPrice=" + totalPrice)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", SavingPointResponse.class);

        // then
        assertAll(
                () -> assertThat(적립된_포인트_응답_객체.getSavingPoint()).isEqualTo(10)
        );
    }
}
