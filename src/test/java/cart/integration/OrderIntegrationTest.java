package cart.integration;

import cart.member.domain.Member;
import cart.member.repository.MemberRepository;
import cart.order.dto.OrderRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;
    
    private Member member;
    
    @BeforeEach
    void setUp() {
        super.setUp();
        
        member = memberRepository.getMemberById(1L);
    }
    
    @Test
    void 장바구니_목록을_주문한다() {
        // given
        final Member member = memberRepository.getMemberById(1L);
        final OrderRequest orderRequest =
                new OrderRequest(List.of(1L), 20000L, memberRepository.getMemberById(1L).getPoint() / 2L, 2000L);
        // expect
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        
        final String orderId = response.header("Location").substring("/orders/".length());
        
        assertThat(Long.parseLong(orderId)).isPositive();
    }
    
    @Test
    void 주문_목록을_조회한다() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 2L),
                        100000L,
                        memberRepository.getMemberById(1L).getPoint() / 2L,
                        10000L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        final OrderRequest orderRequest2 =
                new OrderRequest(List.of(4L), 65000L, memberRepository.getMemberById(1L).getPoint() / 2L, 6500L);
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest2)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        // expect
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
    
    @Test
    void 특정_주문의_상세_정보를_조회한다() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 2L),
                        100000L,
                        memberRepository.getMemberById(1L).getPoint() / 2L,
                        10000L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        final OrderRequest orderRequest2 =
                new OrderRequest(List.of(4L), 65000L, memberRepository.getMemberById(1L).getPoint() / 2L, 6500L);
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest2)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        // expect
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders/" + 2L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
    
    @Test
    void 원가가_일치하지_않을_시_예외_처리() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 2L),
                        100001L,
                        5000L,
                        10000L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", startsWith("[ERROR]"));
    }
    
    @Test
    void 적립될_point가_일치하지_않을_시_예외_처리() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 2L),
                        100000L,
                        5000L,
                        10001L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", startsWith("[ERROR]"));
    }
    
    @Test
    void 사용할_적립금이_적립금_적용_가능한_물품들의_가격_합을_초과할_시_예외_처리() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 2L),
                        100000L,
                        20001L,
                        10000L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", startsWith("[ERROR]"));
    }
    
    @Test
    void 사용할_적립금이_현재_보유한_적립금을_초과할_시_예외_처리() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 4L),
                        85000L,
                        memberRepository.getMemberById(1L).getPoint() + 1L,
                        8500L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", startsWith("[ERROR]"));
    }
    
    @Test
    void 특정_주문의_상세_정보를_조회할_시_주문_번호가_다르면_예외_처리() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(
                        List.of(1L, 2L),
                        100000L,
                        memberRepository.getMemberById(1L).getPoint() / 2L,
                        10000L
                );
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        final OrderRequest orderRequest2 =
                new OrderRequest(List.of(4L), 65000L, memberRepository.getMemberById(1L).getPoint() / 2L, 6500L);
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest2)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        // expect
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders/" + 3L)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", startsWith("[ERROR]"));
    }
}
