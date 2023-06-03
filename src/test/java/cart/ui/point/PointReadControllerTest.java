package cart.ui.point;

import static cart.fixture.MemberFixture.레오_ID포함;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.WebMvcConfig;
import cart.application.repository.MemberRepository;
import cart.application.repository.PointRepository;
import cart.application.service.member.MemberReadService;
import cart.application.service.member.dto.MemberResultDto;
import cart.domain.PointHistory;
import cart.fixture.MemberFixture;
import cart.ui.point.dto.PointResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:/reset.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
class PointReadControllerTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberReadService memberReadService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자별 누적포인트 조회")
    void findPointByMember() {

        MemberResultDto memberById = memberReadService.findMemberById(5L);

        final PointHistory pointHistory = new PointHistory(memberById.getId(), 10, 1000);
        Long pointHistory1 = pointRepository.createPointHistory(memberById.getId(), pointHistory);

        String base64Credentials = java.util.Base64.getEncoder().encodeToString((memberById.getEmail() + ":" + memberById.getPassword()).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/point")
                .then().log().all()
                .extract();
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            PointResponse pointResponse = response.as(PointResponse.class);
            softly.assertThat(pointResponse.getTotalPoint()).usingRecursiveComparison().isEqualTo(990);
        });
    }

}
