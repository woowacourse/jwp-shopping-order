package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import cart.dao.OrderDetailDao;
import cart.dao.OrdersDao;
import cart.dao.PointDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.entity.OrderDetailEntity;
import cart.entity.OrdersEntity;
import cart.entity.PointEntity;
import cart.entity.ProductEntity;
import cart.exception.CartItemNotFoundException;
import cart.exception.NotEnoughPointException;
import cart.exception.NotEnoughStockException;
import cart.exception.PriceNotMatchException;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql", "classpath:insertCartItem.sql", "classpath:insertPoint.sql"})
class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private PointDao pointDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderDetailDao orderDetailDao;

    private Member member1;
    private Member member2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member1 = new Member(1L, "odo1@woowa.com", "1234");
        member2 = new Member(2L, "odo2@woowa.com", "1234");
    }

    @DisplayName("정상적인 주문을 한다.")
    @Test
    void createOrder() {
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L, 3L), 300, 190_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member1, orderRequest);
        final Long orderId = Long.parseLong(response.header("Location").split("/")[2]);
        final JsonPath jsonPath = response.jsonPath();
        final List<PointEntity> pointEntities = pointDao.findRemainingPointsByMemberId(member1.getId());
        final Long pointId = pointEntities.get(2).getId();
        final Optional<OrdersEntity> ordersEntity = ordersDao.findById(orderId);
        final List<OrderDetailEntity> orderDetailEntities = orderDetailDao.findByOrderId(orderId);
        final List<ProductEntity> productEntities = productDao.findAll();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderId).isPositive(),
                () -> assertThat(jsonPath.getLong("orderId")).isPositive(),
                () -> assertThat(jsonPath.getString("createdAt")).isNotNull(),
                () -> assertThat(jsonPath.getList("orderItems")).hasSize(3),
                () -> assertThat(jsonPath.getInt("totalPrice")).isEqualTo(190_000),
                () -> assertThat(jsonPath.getInt("usedPoint")).isEqualTo(300),
                () -> assertThat(jsonPath.getInt("earnedPoint")).isEqualTo(9_500),

                () -> assertThat(pointEntities.get(0).getEarnedPoint()).isEqualTo(500),
                () -> assertThat(pointEntities.get(0).getLeftPoint()).isEqualTo(200),
                () -> assertThat(pointEntities.get(1).getEarnedPoint()).isEqualTo(600),
                () -> assertThat(pointEntities.get(1).getLeftPoint()).isEqualTo(100),
                () -> assertThat(pointEntities.get(2).getEarnedPoint()).isEqualTo(9_500),
                () -> assertThat(pointEntities.get(2).getLeftPoint()).isEqualTo(9_500),

                () -> assertThat(ordersEntity).isPresent(),
                () -> assertThat(ordersEntity.get().getId()).isEqualTo(orderId),
                () -> assertThat(ordersEntity.get().getMemberId()).isEqualTo(member1.getId()),
                () -> assertThat(ordersEntity.get().getPointId()).isEqualTo(pointId),
                () -> assertThat(ordersEntity.get().getEarnedPoint()).isEqualTo(9500),
                () -> assertThat(ordersEntity.get().getUsedPoint()).isEqualTo(300),
                () -> assertThat(ordersEntity.get().getCreatedAt()).isNotNull(),

                () -> assertThat(orderDetailEntities.get(0).getId()).isPositive(),
                () -> assertThat(orderDetailEntities.get(0).getOrdersId()).isEqualTo(orderId),
                () -> assertThat(orderDetailEntities.get(0).getProductId()).isEqualTo(1L),
                () -> assertThat(orderDetailEntities.get(0).getProductName()).isEqualTo("치킨"),
                () -> assertThat(orderDetailEntities.get(0).getProductPrice()).isEqualTo(10_000),
                () -> assertThat(orderDetailEntities.get(0).getProductImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(orderDetailEntities.get(0).getOrderQuantity()).isEqualTo(3),
                () -> assertThat(orderDetailEntities.get(1).getId()).isPositive(),
                () -> assertThat(orderDetailEntities.get(1).getOrdersId()).isEqualTo(orderId),
                () -> assertThat(orderDetailEntities.get(1).getProductId()).isEqualTo(2L),
                () -> assertThat(orderDetailEntities.get(1).getProductName()).isEqualTo("피자"),
                () -> assertThat(orderDetailEntities.get(1).getProductPrice()).isEqualTo(15_000),
                () -> assertThat(orderDetailEntities.get(1).getProductImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(orderDetailEntities.get(1).getOrderQuantity()).isEqualTo(4),
                () -> assertThat(orderDetailEntities.get(2).getId()).isPositive(),
                () -> assertThat(orderDetailEntities.get(2).getOrdersId()).isEqualTo(orderId),
                () -> assertThat(orderDetailEntities.get(2).getProductId()).isEqualTo(3L),
                () -> assertThat(orderDetailEntities.get(2).getProductName()).isEqualTo("샐러드"),
                () -> assertThat(orderDetailEntities.get(2).getProductPrice()).isEqualTo(20_000),
                () -> assertThat(orderDetailEntities.get(2).getProductImageUrl()).isEqualTo("http://example.com/salad.jpg"),
                () -> assertThat(orderDetailEntities.get(2).getOrderQuantity()).isEqualTo(5),

                () -> assertThat(productEntities.get(0).getId()).isEqualTo(1L),
                () -> assertThat(productEntities.get(0).getStock()).isEqualTo(7),
                () -> assertThat(productEntities.get(1).getId()).isEqualTo(2L),
                () -> assertThat(productEntities.get(1).getStock()).isEqualTo(6),
                () -> assertThat(productEntities.get(2).getId()).isEqualTo(3L),
                () -> assertThat(productEntities.get(2).getStock()).isEqualTo(5)
        );
    }

    @DisplayName("클라이언트의 예상 총 가격과 실제 총 가격이 일치하지 않는 주문을 한다.")
    @Test
    void createOrderWhenPriceNotMatch() {
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L, 3L), 100, 100_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member1, orderRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.asString()).isEqualTo(new PriceNotMatchException(100_000, 190_000).getMessage())
        );
    }

    @DisplayName("존재하지 않는 장바구니 품목으로 주문을 한다.")
    @Test
    void createOrderWithNoExistId() {
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 9L), 100, 70_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member1, orderRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.asString()).isEqualTo(new CartItemNotFoundException(9L).getMessage())
        );
    }

    @DisplayName("다른 사람의 장바구니 품목으로 주문을 한다.")
    @Test
    void createOrderWithNotOwned() {
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 6L), 100, 70_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member1, orderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("보유 포인트 보다 사용 포인트가 많은 주문을 한다.")
    @Test
    void createOrderWhenNotEnoughPoint() {
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L, 3L), 601, 190_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member1, orderRequest);
        final JsonPath result = response.jsonPath();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value()),
                () -> assertThat(result.getInt("errorCode")).isEqualTo(2),
                () -> assertThat(result.getString("message")).isEqualTo(new NotEnoughPointException(600, 601).getMessage())
        );
    }

    @DisplayName("재고 보다 많은 수량의 주문을 한다.")
    @Test
    void createOrderWhenNotEnoughStock() {
        final OrderRequest orderRequest = new OrderRequest(List.of(6L, 7L), 100, 205_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member2, orderRequest);
        final JsonPath result = response.jsonPath();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value()),
                () -> assertThat(result.getInt("errorCode")).isEqualTo(1),
                () -> assertThat(result.getString("message")).isEqualTo(new NotEnoughStockException(10, 11).getMessage())
        );
    }

    @DisplayName("삭제된 상품을 담고 있는 장바구니 품목으로 주문을 한다.")
    @Test
    void createOrderWithCartItemIncludingNoExistProduct() {
        productDao.deleteById(3L);
        final OrderRequest orderRequest = new OrderRequest(List.of(6L, 7L), 100, 205_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member2, orderRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.asString()).isEqualTo(new CartItemNotFoundException(6L).getMessage())
        );
    }

    private ExtractableResponse<Response> requestCreateOrder(final Member member, final OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }
}
