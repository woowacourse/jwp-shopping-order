package cart.integration;

import cart.TestFixture;
import cart.application.dto.request.PaymentRequest;
import cart.domain.member.Member;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PayIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        this.member1 = TestFixture.getMember1();
        this.member2 = TestFixture.getMember2();
    }

    @DisplayName("장바구니가 비어있을 경우 bad request를 반환한다.")
    @Test
    void emptyCartPayment() {
        final PaymentRequest request = new PaymentRequest(new ArrayList<>(), 0);

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(request)
                .when()
                .post("/pay")
                .then()
                .extract();


        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.asString()).isEqualTo("장바구니가 비어있습니다.")
        );
    }

    @DisplayName("포인트가 0보다 작을 경우 bad request를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -100, -987654321})
    void minusPointPayment(final int point) {
        final PaymentRequest request = new PaymentRequest(List.of(1L), point);

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(request)
                .when()
                .post("/pay")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.asString()).isEqualTo("포인트는 0원 이상 사용 가능합니다.")
        );
    }

    @DisplayName("사용자의 보유 포인트보다 사용 포인트가 클 경우 bad request를 반환한다.")
    @Test
    void overPointUsePayment() {
        final PaymentRequest request = new PaymentRequest(List.of(1L), 100);

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(request)
                .when()
                .post("/pay")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.asString()).isEqualTo("포인트가 부족합니다.")
        );
    }

    @DisplayName("장바구니 아이템을 담은 사용자와 결제 요청 사용자가 다를 경우 bad request를 반환한다.")
    @Test
    void differentMemberPayment() {
        final PaymentRequest request = new PaymentRequest(List.of(1L), 100);

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .body(request)
                .when()
                .post("/pay")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(response.asString()).isEqualTo("장바구니 아이템 소유자와 사용자가 다릅니다.")
        );
    }

    @DisplayName("장바구니에 담긴 아이템을 결제한다.")
    @Test
    void paymentCartItems() {
        final PaymentRequest request = new PaymentRequest(List.of(1L), 0);
        final int expectPoint = 1_000;

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(request)
                .when()
                .redirects().follow(false)
                .post("/pay")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("redirect:/members/orders/1"),
                // point = price * quantity * point_rate
                () -> assertThat(memberDao.findByMemberId(member1.getId()).getPoint()).isEqualTo(expectPoint)
        );
    }

    @DisplayName("포인트를 사용할 경우 포인트가 차감된다.")
    @Test
    void paymentWithPoint() {
        final int expectPoint = 1400;
        final PaymentRequest request = new PaymentRequest(List.of(1L), 100);
        member1.savePoint(500);
        memberDao.updatePoint(new MemberEntity(member1.getId(), member1.getEmail(), member1.getPassword(), member1.getPoint()));

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(request)
                .when()
                .redirects().follow(false)
                .post("/pay")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("redirect:/members/orders/1"),
                () -> assertThat(memberDao.findByMemberId(member1.getId()).getPoint()).isEqualTo(expectPoint)
        );
    }
}
