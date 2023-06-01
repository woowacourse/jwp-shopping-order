package cart.ui;

import cart.config.ControllerTestConfig;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.order.OrderProductsRequest;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.headers.HeaderDocumentation;

import java.util.List;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

//@AutoConfigureMockMvc
//@AutoConfigureRestDocs
public class OrderApiControllerTest extends ControllerTestConfig {

    private static final String USERNAME = "a@a.com";
    private static final String PASSWORD = "1234";
    private static final String DOCUMENT_IDENTIFIER = "{method-name}";

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CartItemDao cartItemDao;

    private Member member;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        member = memberDao.getMemberById(1L);

        cartItem1 = cartItemDao.findById(1L);
        cartItem1 = cartItemDao.findById(2L);
    }

    @Nested
    class showOrderProductHistories {

        @Test
        void 상품을_조회한다() {
            List<Long> 주문상품들 = List.of(cartItem1.getProduct().getId(), cartItem2.getProduct().getId());
            int 사용_포인트 = 1_000;
            OrderProductsRequest orderProductsRequest = new OrderProductsRequest(주문상품들, 사용_포인트);
            장바구니_상품_주문(member, orderProductsRequest);

            given(spec)
                    .log().all()
                    .filter(document(DOCUMENT_IDENTIFIER,
                            requestHeaders(
                                    HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
                            ),
                            responseFields(
                                    fieldWithPath("[].orderId").description("장바구니 상품 식별자값"),
                                    fieldWithPath("[].orderProducts").description("장바구니 상품 수량"),
                                    fieldWithPath("[].product.id").description("상품 식별자값"),
                                    fieldWithPath("[].product.name").description("상품명"),
                                    fieldWithPath("[].product.price").description("상품 가격"),
                                    fieldWithPath("[].product.quantity").description("상품 수량"),
                                    fieldWithPath("[].product.imageUrl").description("상품 이미지 주소"),
                                    fieldWithPath("[].product.totalPrice").description("총 금액")
                            )))
                    .contentType(APPLICATION_JSON_VALUE)
                    .when()
                    .log().all()
                    .auth().preemptive().basic(USERNAME, PASSWORD)
                    .get("/orders")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    @Nested
    class orderProducts {

        @Test
        void 상품을_주문한다() {
            given(spec)
                    .log().all()
                    .filter(document(DOCUMENT_IDENTIFIER,
                            requestHeaders(
                                    HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
                            ),
                            requestFields(
                                    // TODO : cartIds 여러개 담는 방법 변경
                                    fieldWithPath("[].cartIds").description("장바구니 아이디들"),
                                    fieldWithPath("points").description("사용된 포인트")
                            )))
                    .contentType(APPLICATION_JSON_VALUE)
                    .when()
                    .log().all()
                    .auth().preemptive().basic(USERNAME, PASSWORD)
                    .get("/orders")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }
    }

    @Nested
    class showOrderHistoryById {

        @Test
        void 특정_아이디_주문상품을_조회한다() {
            List<Long> 주문상품들 = List.of(cartItem1.getProduct().getId(), cartItem2.getProduct().getId());
            int 사용_포인트 = 1_000;
            OrderProductsRequest orderProductsRequest = new OrderProductsRequest(주문상품들, 사용_포인트);
            long 주문_아이디 = 장바구니_상품_주문(member, orderProductsRequest);

            given(spec)
                    .log().all()
                    .filter(document(DOCUMENT_IDENTIFIER,
                            requestHeaders(
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
                            ),
                            responseFields(
                                    fieldWithPath("[].orderId").description("장바구니 상품 식별자값"),
                                    fieldWithPath("[].orderProducts").description("장바구니 상품 수량"),
                                    fieldWithPath("[].orderTotalPrice").description("주문 총 금액"),
                                    fieldWithPath("[].usedPoint").description("사용한 포인트"),
                                    fieldWithPath("[].createdAt").description("주문 일자"),
                                    fieldWithPath("[].product.id").description("상품 식별자값"),
                                    fieldWithPath("[].product.name").description("상품명"),
                                    fieldWithPath("[].product.price").description("상품 가격"),
                                    fieldWithPath("[].product.quantity").description("상품 수량"),
                                    fieldWithPath("[].product.imageUrl").description("상품 이미지 주소"),
                                    fieldWithPath("[].product.totalPrice").description("총 금액")
                            )))
                    .contentType(APPLICATION_JSON_VALUE)
                    .when()
                    .log().all()
                    .auth().preemptive().basic(USERNAME, PASSWORD)
                    .get("/orders/" + 주문_아이디)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    private long 장바구니_상품_주문(Member member, OrderProductsRequest orderProductsRequest) {
        System.out.println("\n!!!! orderApiCOntrollerTest() 반환값 변경 필요\n");
        return 0L;
    }
}
