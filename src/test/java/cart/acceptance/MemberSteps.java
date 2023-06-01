package cart.acceptance;

import static io.restassured.RestAssured.given;

import cart.domain.Member;
import cart.ui.MemberRequest;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class MemberSteps {
    public static void 유저_생성_요청(String email, String password) {
        MemberRequest memberRequest = new MemberRequest(email, password);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/members")
                .then();
    }

    public static Member 유저_생성_요청하고_유저_반환(String email, String password) {
        유저_생성_요청(email, password);
        return new Member(email, password);
    }
}
