package cart.ui.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.ProductRepository;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.fixture.MemberFixture;
import cart.fixture.ProductFixture;
import cart.ui.order.dto.CreateOrderDiscountRequest;
import cart.ui.order.dto.CreateOrderItemRequest;
import cart.ui.order.dto.CreateOrderRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.MemberFixture.레오;
import static cart.fixture.ProductFixture.배변패드;
import static cart.fixture.ProductFixture.통구이;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/reset.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long leoId;
    private long dinoId;
    private long beaverId;

    private long bbqProductId;
    private long padProductId;
    private long tailProductId;

    private long usablePercentCoupon;
    private long usableAmountCoupon;

    private long padCartItem;
    private long bbqCartItem;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        leoId = memberRepository.createMember(레오);
        dinoId = memberRepository.createMember(MemberFixture.디노);

        createCoupon();
        createProduct();
        createCartItem();
    }

    @Test
    @DisplayName("상품을 주문한다.")
    public void findOrders() {
        CreateOrderItemRequest bbqCart = new CreateOrderItemRequest(bbqCartItem, bbqProductId, 3);
        CreateOrderItemRequest padCart = new CreateOrderItemRequest(padCartItem, padProductId, 6);

        CreateOrderDiscountRequest orderDiscounts = new CreateOrderDiscountRequest(List.of(), 3000);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(bbqCart, padCart), orderDiscounts);
        Response response = given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest)
                .when().post("/orders")
                .then().log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("존재하지 않는 장바구니로 주문 요청을 하면 예외발생")
    public void wrongOrder() {
        CreateOrderItemRequest wrongCart = new CreateOrderItemRequest(bbqCartItem, bbqProductId, 3);
        CreateOrderItemRequest padCart = new CreateOrderItemRequest(padCartItem, padProductId, 6);

        CreateOrderDiscountRequest orderDiscounts = new CreateOrderDiscountRequest(List.of(usablePercentCoupon), 3000);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(wrongCart, padCart), orderDiscounts);

        // 주문요청으로 인해 해당 장바구니 삭제
        given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest)
                .when().post("/orders")
                .then().log().all();

        Response response = given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest)
                .when().post("/orders")
                .then().log().all()
                .extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("유효하지 않은 사용자로 주문 요청하면 예외발생")
    public void invalidMemberOrder() {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .when().post("/orders")
                .then().log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("이미 사용한 쿠폰으로 재주문 시 예외발생")
    public void invalidCouponOrder() {
        CreateOrderItemRequest bbq = new CreateOrderItemRequest(bbqCartItem, bbqProductId, 3);
        CreateOrderItemRequest padCart = new CreateOrderItemRequest(padCartItem, padProductId, 6);

        CreateOrderDiscountRequest orderDiscounts1 = new CreateOrderDiscountRequest(List.of(usablePercentCoupon), 3000);
        CreateOrderRequest createOrderRequest1 = new CreateOrderRequest(List.of(padCart), orderDiscounts1);

        // 주문요청으로 인해 해당 장바구니 삭제
        given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest1)
                .when().post("/orders")
                .then().log().all();

        CreateOrderDiscountRequest orderDiscounts2 = new CreateOrderDiscountRequest(List.of(usablePercentCoupon), 3000);
        CreateOrderRequest createOrderRequest2 = new CreateOrderRequest(List.of(bbq), orderDiscounts2);

        Response response = given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest2)
                .when().post("/orders")
                .then().log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("사용자 전체 주문 목록을 조회한다.")
    public void findMemberOrders() {
        CreateOrderItemRequest bbqCart = new CreateOrderItemRequest(bbqCartItem, bbqProductId, 3);
        CreateOrderItemRequest padCart = new CreateOrderItemRequest(padCartItem, padProductId, 6);

        CreateOrderDiscountRequest orderDiscounts1 = new CreateOrderDiscountRequest(List.of(usablePercentCoupon), 3000);

        CreateOrderRequest createOrderRequest1 = new CreateOrderRequest(List.of(bbqCart), orderDiscounts1);
        given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest1)
                .when().post("/orders")
                .then().log().all();


        CreateOrderDiscountRequest orderDiscounts2 = new CreateOrderDiscountRequest(List.of(), 3000);
        CreateOrderRequest createOrderRequest2 = new CreateOrderRequest(List.of(padCart), orderDiscounts2);
        given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest2)
                .when().post("/orders")
                .then().log().all();

        Response response = given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .when().get("/orders")
                .then().log().all()
                .body("orderResponses[0].paymentPrice", equalTo(0))
                .body("orderResponses[0].usedPoint", equalTo(3000))
                .body("orderResponses[1].paymentPrice", equalTo(57000))
                .body("orderResponses[1].usedPoint", equalTo(3000))
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("사용자 특정 주문을 조회한다")
    public void findOneOrder() {
        CreateOrderItemRequest bbqCart = new CreateOrderItemRequest(bbqCartItem, bbqProductId, 3);
        CreateOrderItemRequest padCart = new CreateOrderItemRequest(padCartItem, padProductId, 6);

        CreateOrderDiscountRequest orderDiscounts1 = new CreateOrderDiscountRequest(List.of(usablePercentCoupon), 3000);
        CreateOrderRequest createOrderRequest1 = new CreateOrderRequest(List.of(bbqCart), orderDiscounts1);

        String location = given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest1)
                .when().post("/orders")
                .then().log().all()
                .extract().header("location");

        String[] splitHeader = location.split("/");
        long orderId = Long.parseLong(splitHeader[1]);

        CreateOrderDiscountRequest orderDiscounts2 = new CreateOrderDiscountRequest(List.of(), 3000);
        CreateOrderRequest createOrderRequest2 = new CreateOrderRequest(List.of(padCart), orderDiscounts2);
        given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .contentType(ContentType.JSON)
                .body(createOrderRequest2)
                .when().post("/orders")
                .then().log().all();

        Response response = given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .when().get("/orders/{orderId}", orderId)
                .then().log().all()
                .body("usedPoint", equalTo(3000))
                .body("paymentPrice", equalTo(0))
                .body("orderItems[0].productName", equalTo("디노통구이"))
                .body("usedCoupons[0].couponName", equalTo("웰컴 쿠폰 - 10%할인"))
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    private void createCoupon() {

        SimpleJdbcInsert simpleCouponJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
        MapSqlParameterSource welcomeCoupon = new MapSqlParameterSource()
                .addValue("name", "웰컴 쿠폰 - 10%할인")
                .addValue("min_amount", 10000)
                .addValue("discount_percent", 10)
                .addValue("discount_amount", 0);

        MapSqlParameterSource againCoupon = new MapSqlParameterSource()
                .addValue("name", "또 와요 쿠폰 - 3000원 할인")
                .addValue("min_amount", 15000)
                .addValue("discount_percent", 0)
                .addValue("discount_amount", 3000);

        long welcomeCouponId = simpleCouponJdbcInsert.executeAndReturnKey(welcomeCoupon).longValue();
        long againCouponId = simpleCouponJdbcInsert.executeAndReturnKey(againCoupon).longValue();

        createMemberCoupon(welcomeCouponId, againCouponId);
    }

    private void createMemberCoupon(long welcomeCouponId, long againCouponId) {
        SimpleJdbcInsert simpleMemberCouponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");

        MapSqlParameterSource leoUsableCoupon1 = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("coupon_id", welcomeCouponId)
                .addValue("status", 1);

        MapSqlParameterSource leoUsableCoupon2 = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("coupon_id", againCouponId)
                .addValue("status", 1);

        usablePercentCoupon = simpleMemberCouponInsert.executeAndReturnKey(leoUsableCoupon1).longValue();
        usableAmountCoupon = simpleMemberCouponInsert.executeAndReturnKey(leoUsableCoupon2).longValue();
    }

    private void createProduct() {
        bbqProductId = productRepository.createProduct(통구이);
        padProductId = productRepository.createProduct(ProductFixture.배변패드);
        tailProductId = productRepository.createProduct(ProductFixture.꼬리요리);
    }

    private void createCartItem() {
        Product bbq = new Product(bbqProductId, 통구이.getName(), 통구이.getPrice(), 통구이.getImageUrl());
        Product pad = new Product(padProductId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());

        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());

        bbqCartItem = cartItemRepository.createCartItem(new CartItem(3, bbq, leo));
        padCartItem = cartItemRepository.createCartItem(new CartItem(6, pad, leo));
    }

}
