package cart.integration;

import cart.domain.member.Member;
import cart.domain.repository.MemberRepository;
import cart.ui.member.dto.request.MemberJoinRequest;
import cart.ui.member.dto.request.MemberLoginRequest;
import cart.util.Encryptor;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void joinTest() {
        MemberJoinRequest request = new MemberJoinRequest("testMember", "asdf1234");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/users/join")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("로그인을 할 수 있다.")
    @Test
    void loginTest() {
        //given
        String name = "testMember";
        String password = "test1234";
        MemberLoginRequest request = new MemberLoginRequest(name, password);
        Member member = new Member(name, password);

        memberRepository.save(member);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/users/login")
                .then()
                .log().all()
                .extract();

        //then
        String encryptedPassword = Encryptor.encrypt(password);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("password")).isEqualTo(encryptedPassword);
    }
}
