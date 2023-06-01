package cart.integration;

import cart.member.domain.Member;
import cart.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@SuppressWarnings("NonAsciiCharacters")
class PointIntegratedTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;
    
    private Member member;
    
    @BeforeEach
    void setUp() {
        super.setUp();
        
        member = memberRepository.getMemberById(1L);
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
