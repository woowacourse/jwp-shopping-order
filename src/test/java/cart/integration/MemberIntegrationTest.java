package cart.integration;

import static cart.exception.ErrorCode.INVALID_REQUEST;
import static cart.exception.ErrorCode.MEMBER_DUPLICATE_NAME;
import static cart.exception.ErrorCode.MEMBER_NOT_FOUND;
import static cart.exception.ErrorCode.MEMBER_PASSWORD_INVALID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import cart.application.dto.coupon.CouponRequest;
import cart.application.dto.member.MemberJoinRequest;
import cart.application.dto.member.MemberLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("사용자를 추가한다.")
    void join() {
        // given
        /** 쿠폰 저장 */
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);
        쿠폰_저장(신규_가입_쿠폰_등록_요청);

        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "password");
        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest("journey", "password");

        // when
        given()
            .when()
            .body(져니_저장_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/join")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header(LOCATION, "/users/" + 1);

        // then
        /** 사용자 조회 */
        given()
            .when()
            .get("/users/{id}", 1)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("name", equalTo("journey"))
            .body("password", equalTo("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"));

        /** 가입 후 신규 회원 축하 쿠폰이 발급되었는지 확인 */
        given()
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .get("/users/me/coupons")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(1))
            .body("[0].id", equalTo(1))
            .body("[0].name", equalTo("신규 가입 축하 쿠폰"))
            .body("[0].discountRate", equalTo(20))
            .body("[0].isUsed", equalTo(false));
    }

    @Test
    @DisplayName("사용자 추가 시 비어있는 정보로 요청이 들어오면 예외가 발생한다.")
    void join_invalid_request() {
        // given
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("", "");

        // expected
        given()
            .when()
            .body(져니_저장_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/join")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("이름에 빈 값을 입력할 수 없습니다.",
                "사용자 비밀번호는 비어있을 수 없습니다."));
    }

    @Test
    @DisplayName("사용자 추가 시 이미 존재하는 이름으로 요청이 들어오면 예외가 발생한다.")
    void join_duplicated_name() {
        // given
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "password");
        사용자_저장(져니_저장_요청);

        final MemberJoinRequest 또다른_져니_저장_요청 = new MemberJoinRequest("journey", "password22");

        // expected
        given()
            .when()
            .body(또다른_져니_저장_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/join")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(MEMBER_DUPLICATE_NAME.name()))
            .body("errorMessage", equalTo("이미 등록된 사용자 이름입니다."));
    }

    @Test
    @DisplayName("사용자 로그인을 진행한다.")
    void login() {
        // given
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "password");
        사용자_저장(져니_저장_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest("journey", "password");

        // expected
        given()
            .when()
            .body(져니_로그인_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("token", equalTo("am91cm5leTpwYXNzd29yZA=="));
    }

    @Test
    @DisplayName("사용자 로그인 시 존재하지 않은 사용자라면 예외가 발생한다.")
    void login_invalid_member() {
        // given
        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest("journey", "password");

        // expected
        given()
            .when()
            .body(져니_로그인_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/login")
            .then()
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.name()))
            .body("errorMessage", equalTo("사용자 정보를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("사용자 로그인 시 잘못된 비밀번호로 요청이 들어오면 예외가 발생한다.")
    void login_invalid_password() {
        // given
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "password");
        사용자_저장(져니_저장_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest("journey", "password2");

        // expected
        given()
            .when()
            .body(져니_로그인_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/login")
            .then()
            .body("errorCode", equalTo(MEMBER_PASSWORD_INVALID.name()))
            .body("errorMessage", equalTo("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("사용자 정보를 조회한다.")
    void getMember() {
        // given
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "password");
        사용자_저장(져니_저장_요청);

        // expected
        given()
            .when()
            .get("/users/{id}", 1)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("name", equalTo("journey"))
            .body("password", equalTo("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"));
    }

    @Test
    @DisplayName("전체 사용자 정보를 조회한다.")
    void getMembers() {
        // given
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "jourzura1");
        final MemberJoinRequest 라온_저장_요청 = new MemberJoinRequest("raon", "jourzura2");
        final MemberJoinRequest 쥬니_저장_요청 = new MemberJoinRequest("zuny", "jourzura3");
        사용자_저장(져니_저장_요청);
        사용자_저장(라온_저장_요청);
        사용자_저장(쥬니_저장_요청);

        // expected
        given()
            .when()
            .get("/users")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(3))
            .body("[0].id", equalTo(1))
            .body("[0].name", equalTo("journey"))
            .body("[0].password", equalTo("3358e5f07f2888c949e91f4de27072607ee80febe4edcecebc3283c83a2d7216"))
            .body("[1].id", equalTo(2))
            .body("[1].name", equalTo("raon"))
            .body("[1].password", equalTo("bf4f6c7ecd0343d02d61c2ceb781e296cc9feddac1331d1ea5151b7dba028594"))
            .body("[2].id", equalTo(3))
            .body("[2].name", equalTo("zuny"))
            .body("[2].password", equalTo("9b58103a20cc378a1855bd16fddf719f8048b2897e13770c03edeb748a41b2a5"));
    }

    @Test
    @DisplayName("사용자의 쿠폰 목록을 조회한다.")
    void getMyCoupons() {
        // given
        /** 쿠폰 저장 */
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);
        쿠폰_저장(신규_가입_쿠폰_등록_요청);

        /** 사용자 저장 */
        final MemberJoinRequest 져니_저장_요청 = new MemberJoinRequest("journey", "jourzura1");
        사용자_저장(져니_저장_요청);

        final MemberLoginRequest 져니_로그인_요청 = new MemberLoginRequest(져니_저장_요청.getName(), 져니_저장_요청.getPassword());

        // expected
        given()
            .auth().preemptive().basic(져니_로그인_요청.getName(), 져니_로그인_요청.getPassword())
            .when()
            .get("/users/me/coupons")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(1))
            .body("[0].id", equalTo(1))
            .body("[0].name", equalTo("신규 가입 축하 쿠폰"))
            .body("[0].discountRate", equalTo(20))
            .body("[0].isUsed", equalTo(false));
    }
}
