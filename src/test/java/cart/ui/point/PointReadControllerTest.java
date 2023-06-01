package cart.ui.point;

import cart.WebMvcConfig;
import cart.application.repository.PointRepository;
import cart.domain.PointHistory;
import cart.fixture.MemberFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:/reset.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
class PointReadControllerTest {

    @Autowired
    private PointRepository pointRepository;

    @MockBean
    WebMvcConfig webMvcConfig;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자별 누적포인트 조회")
    void findPointByMember() {
        String email = "leo@gmail.com";
        String password = "leo123";
        final PointHistory pointHistory = new PointHistory(1L, 1000, 0);
        pointRepository.createPointHistory(MemberFixture.디노_ID포함.getId(), pointHistory);

        String base64Credentials = java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/point")
                .then().log().all()
                .extract();
// TODO : 리스폰스 값 검증
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );

    }
}
