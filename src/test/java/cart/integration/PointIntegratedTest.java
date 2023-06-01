package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
class PointIntegratedTest extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;
    
    private Member member;
    
    @BeforeEach
    void setUp() {
        super.setUp();
        
        member = memberDao.getMemberById(1L);
    }
    
    @Test
    public void 해당_회원의_포인트를_조회한다() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/point")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("point", greaterThan(0));
    }
}
