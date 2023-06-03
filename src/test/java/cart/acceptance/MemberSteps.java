package cart.acceptance;

import static io.restassured.RestAssured.given;

import cart.common.dto.MemberRequest;
import cart.member.application.Member;
import cart.member.application.Point;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class MemberSteps {
    public static void 유저_생성_요청(String email, String password, int point) {
        MemberRequest memberRequest = new MemberRequest(email, password, point);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/members")
                .then();
    }

    public static Member 유저_생성_요청하고_유저_반환(String email, String password, int point) {
        유저_생성_요청(email, password, point);
        return new Member(email, password, new Point(point));
    }
}
