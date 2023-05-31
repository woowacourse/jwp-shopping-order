package cart.integration;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.dto.PointResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.common.fixture.DomainFixture.EMAIL;
import static cart.common.fixture.DomainFixture.PASSWORD;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class PointIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 회원의_포인트를_조회한다() {
        //given
        memberDao.addMember(new MemberEntity(EMAIL, PASSWORD, 1000));

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when()
                .get("/points")
                .then().log().all()
                .extract();

        //then
        final PointResponse pointResponse = response.as(PointResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(pointResponse).usingRecursiveComparison()
                    .isEqualTo(new PointResponse(1000));
        });
    }
}
