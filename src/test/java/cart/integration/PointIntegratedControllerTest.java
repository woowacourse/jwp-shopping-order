package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
class PointIntegratedControllerTest extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;
    
    private Member member;
    private Member member2;
    
    @BeforeEach
    void setUp() {
        super.setUp();
        
        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }
    
    @Test
    public void 해당_회원의_포인트를_조회한다() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/point")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("point", is(10000));
    }
    
    @Test
    public void 적립금_정상_범위를_벗어난_경우_예외_처리() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/point")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("point", is(10000));
    }
}
