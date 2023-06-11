package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.PointResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;

public class PointIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @DisplayName("특정 회원의 포인트 현황을 조회할 수 있다")
    @Test
    void getPoint() {
        PointResponse expectResponse = new PointResponse(2200, 1200);

        PointResponse result = given().log().all()
                            .auth().preemptive().basic(member.getEmail(), member.getPassword())
                            .when()
                            .get("/points")
                            .then()
                            .log().all()
                            .extract()
                            .jsonPath()
                            .getObject(".", PointResponse.class);

        assertThat(result).isEqualTo(expectResponse);
    }
}
