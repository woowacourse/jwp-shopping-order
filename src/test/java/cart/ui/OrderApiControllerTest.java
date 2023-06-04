package cart.ui;

import cart.config.ControllerTestConfig;
import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.order.OrderProductsRequest;
import cart.repository.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

public class OrderApiControllerTest extends ControllerTestConfig {

    private static final String USERNAME = "a@a.com";
    private static final String PASSWORD = "1234";
    private static final String DOCUMENT_IDENTIFIER = "{method-name}";
    private static final int 배달비 = 3_000;

    Product 상품_계란_등록() {
        final Product product = new Product("계란", 1000, "https://계란_이미지_주소.png");
        final Long 상품_계란_식별자값 = productDao.createProduct(product);
        return new Product(상품_계란_식별자값, product.getName(), product.getPrice(), product.getImageUrl());
    }

    Member 회원_등록() {
        final Member member = new Member("a@a.com", "1234");
        long memberId = memberDao.addMember(member);
        return new Member(memberId, "a@a.com", "1234");
    }

    CartItem 장바구니_등록(final Product 상품, final Member 회원) {
        final Long 장바구니_상품_식별자값 = cartItemDao.save(new CartItem(회원, 상품));
        return new CartItem(장바구니_상품_식별자값, 1, 상품, 회원);
    }

    private Member member;
    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        member = 회원_등록();

        Product 계란 = 상품_계란_등록();

        cartItem1 = 장바구니_등록(계란, member);
        cartItem2 = 장바구니_등록(계란, member);
    }

    @Test
    void 상품을_조회한다() {
        List<Long> 주문상품들 = List.of(cartItem1.getId());
        int 사용_포인트 = 1_000;
        OrderProductsRequest orderProductsRequest = new OrderProductsRequest(주문상품들, 사용_포인트, 배달비);
        장바구니_상품_주문(member, orderProductsRequest);

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
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

    @Test
    void 상품을_주문한다() {
        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
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

    @Test
    void 특정_아이디_주문상품을_조회한다() {
        List<Long> 주문상품들 = List.of(cartItem1.getId(), cartItem2.getId());
        int 사용_포인트 = 1_000;
        OrderProductsRequest orderProductsRequest = new OrderProductsRequest(주문상품들, 사용_포인트, 배달비);
        long 주문_아이디 = 장바구니_상품_주문(member, orderProductsRequest);

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
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

    private long 장바구니_상품_주문(Member member, OrderProductsRequest orderProductsRequest) {
        Integer totalPrice = orderProductsRequest.getCartIds()
                .stream()
                .map(cartId -> {
                    CartItem cartItem = cartItemDao.findById(cartId);
                    return cartItem.getProduct().getPrice() * cartItem.getQuantity();
                }).reduce(Integer::sum)
                .get();
        OrderEntity orderEntity = new OrderEntity.Builder()
                .memberId(member.getId())
                .totalPayment(totalPrice + orderProductsRequest.getDeliveryFee() - orderProductsRequest.getPoint())
                .usedPoint(orderProductsRequest.getPoint())
                .build();
        return orderDao.insertOrder(orderEntity);
    }
}
