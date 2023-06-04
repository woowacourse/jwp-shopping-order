//package cart.integration;
//
//import cart.dto.MemberResponse;
//import cart.dao.MemberDao;
//import cart.domain.Member;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//import static io.restassured.RestAssured.given;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class MemberIntegrationTest extends IntegrationTest{
//
//    @Autowired
//    private MemberDao memberDao;
//    private Member member2;
//
//    @BeforeEach
//    void setUp() {
//        super.setUp();
//        member2 = memberDao.getMemberById(2L); //골드 등급
//    }
//
//    @Test
//    @DisplayName("멤버의 등급을 조회한다")
//    public void getMemberRank() {
//        var result = given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
//                .when()
//                .get("/member")
//                .then()
//                .extract();
//
//        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    @Test
//    @DisplayName("멤버의 등급을 조회하고 올바른 등급인지 확인한다")
//    public void getMemberRank_validate() {
//        MemberResponse response = given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
//                .when()
//                .get("/member")
//                .then()
//                .extract()
//                .jsonPath()
//                .getObject(".", MemberResponse.class);
//
//        assertThat(response.getRank()).isEqualTo("gold");
//    }
//}
