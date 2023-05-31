package cart.integration;

import cart.dao.*;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Point;
import cart.domain.Product;
import cart.dto.ExceptionResponse;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrdersIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private PointDao pointDao;
    @Autowired
    private ProductDao productDao;

    private Member member;
    private Member member2;
    private Long cartItem1;
    private int cart1Quantity;
    private int cart2Quantity;
    private Long cartItem2;
    private Long cartItem3;
    private int beforePoint;
    private Product product1;
    private Product product2;
    private Product product3;

    @DisplayName("더미 데이터")
    @BeforeEach
    void setUp() {
        super.setUp();

        memberDao.addMember(new Member(null, "test@test.com", "test"));
        member = memberDao.getMemberByEmail("test@test.com").get();

        memberDao.addMember(new Member(null, "test2@test.com", "test2"));
        member2 = memberDao.getMemberByEmail("test2@test.com").get();

        Long productId1 = productDao.createProduct(new Product("product1", 1000, "test1", 10));
        product1 = productDao.getProductById(productId1).get();
        cart1Quantity = 1;
        cartItem1 = cartItemDao.save(new CartItem(member, product1));

        Long productId2 = productDao.createProduct(new Product("product2", 2000, "test2", 10));
        product2 = productDao.getProductById(productId2).get();
        cart2Quantity = 2;
        cartItem2 = cartItemDao.save(new CartItem(null, cart2Quantity, product2, member));

        Long productId3 = productDao.createProduct(new Product("sold out", 3000, "test3", 0));
        product3 = productDao.getProductById(productId3).get();
        cartItem3 = cartItemDao.save(new CartItem(member, product3));

        beforePoint = 2000;
        pointDao.createPoint(new Point(beforePoint, beforePoint, member, LocalDateTime.now().plusDays(3), LocalDateTime.now()));
    }

    @DisplayName("주문한다.")
    @Test
    void orderSuccess() {
        //given
        int usePoint = 100;
        OrderRequest orderRequest = new OrderRequest(List.of(cartItem1, cartItem2), usePoint, 5000);

        int beforeStock1 = product1.getStock();
        int beforeStock2 = product1.getStock();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(cartItemDao.findByMemberId(member.getId()).stream().mapToLong(CartItem::getId)).doesNotContain(cartItem1, cartItem2);
        assertThat(pointDao.getBeforeExpirationAndRemainingPointsByMemberId(member.getId()).stream().mapToInt(Point::getLeftPoint).sum()).isEqualTo(beforePoint - usePoint);
        assertThat(productDao.getProductById(product1.getId()).get().getStock()).isEqualTo(beforeStock1 - cart1Quantity);
        assertThat(productDao.getProductById(product2.getId()).get().getStock()).isEqualTo(beforeStock2 - cart2Quantity);
    }

    @DisplayName("재고 이상으로 구매할 수 없다.")
    @Test
    void orderFailUnderStock() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(cartItem3), 100, 3000);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(exceptionResponse.getMessage()).endsWith("의 재고가 부족합니다.");
        assertThat(exceptionResponse.getErrorCode()).isEqualTo(1);
    }

    @DisplayName("가진 포인트 이상으로 사용할 수 없다.")
    @Test
    void orderFailOverRemainingPoint() {
        // given
        List<Point> remainingPoint = pointDao.getBeforeExpirationAndRemainingPointsByMemberId(member.getId());
        int sum = remainingPoint.stream().mapToInt(Point::getLeftPoint).sum();

        OrderRequest orderRequest = new OrderRequest(List.of(cartItem1), sum + 1000, 5000);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("포인트가 부족합니다.");
        assertThat(exceptionResponse.getErrorCode()).isEqualTo(2);
    }

    @DisplayName("예상 금액보다 포인트를 더 많이 사용할 수 없다.")
    @Test
    void orderFailPointOverTotalAmount() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(cartItem1), 2000, 1000);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("다른 멤버의 장바구니로 주문을 실패한다.")
    @Test
    void orderFailCartInAnotherMember() {
        OrderRequest orderRequest = new OrderRequest(List.of(cartItem1), 0, 1000);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("존재하지 않는 장바구니의 경우 주문을 실패한다.")
    @Test
    void orderFailNotExistCart() {
        OrderRequest orderRequest = new OrderRequest(List.of(99999999999L), 0, 1000);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }


    @DisplayName("멤버의 주문들을 조회한다.")
    @Test
    void retrieveOrdersInMember() {
        //given
        OrderRequest orderRequest = new OrderRequest(List.of(cartItem1, cartItem2), 0, 5000);
        order(member, orderRequest);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        // then
        List<OrderDetailResponse> ordersResponse = response.jsonPath().getList("", OrderDetailResponse.class);
        assertThat(ordersResponse.size()).isEqualTo(1);
    }

    @DisplayName("주문의 상세 정보를 조회한다.")
    @Test
    void retrieveOrderDetailInOrder() {
        //given
        int totalPrice = 5000;
        OrderRequest orderRequest = new OrderRequest(List.of(cartItem1, cartItem2), 0, totalPrice);
        ExtractableResponse<Response> orderInsertResponse = order(member, orderRequest);
        String location = orderInsertResponse.header("Location");

        // when
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .get(location)
                .then()
                .log().all()
                .extract();

        // then
        OrderDetailResponse ordersResponse = response.as(OrderDetailResponse.class);
        assertThat(ordersResponse.getTotalPrice()).isEqualTo(totalPrice);
    }

    @DisplayName("주문한다.")
    private ExtractableResponse<Response> order(Member member, OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }
}
