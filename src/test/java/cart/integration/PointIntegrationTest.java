package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.PointResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PointIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @DisplayName("가용 포인트를 조회한다")
    @Test
    void getUserPoints() {
        member = memberDao.getMemberByEmail("kangsj9665@gmail.com");
        member2 = memberDao.getMemberByEmail("yis092521@gmail.com");

        memberDao.updatePoints(300L, member);

        PointResponse point = requestGetPoints(member);

        assertThat(point.getPoint()).isEqualTo(300L);
    }

    @DisplayName("적립되는 포인트를 조회한다")
    @Test
    void getSavingPoints() {

    }

    private PointResponse requestGetPoints(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(PointResponse.class);
    }
}
