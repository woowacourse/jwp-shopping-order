package cart.ui.api;

import cart.config.ControllerTestConfig;
import cart.domain.member.Member;
import org.junit.jupiter.api.Test;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class MemberApiControllerTest extends ControllerTestConfig {

    private static final String USERNAME = "a@a.com";
    private static final String PASSWORD = "1234";

    @Test
    void 회원_식별자값으로_회원을_조회한다() {
        Member 회원 = memberDaoFixture.회원을_등록한다(USERNAME, PASSWORD, "10000", "1000");

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("basic 64인코딩값")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 식별자값"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("money").description("회원 보유 금액"),
                                fieldWithPath("point").description("회원 보유 포인트")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .log().all()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .get("/members")
        .then()
                .log().all()
                .statusCode(OK.value());
    }
}
