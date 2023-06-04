package cart.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.Quantity;
import cart.dto.CartItemInfoRequest;
import cart.dto.OrderRequest;
import cart.dto.ProductInfoRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderIntegrationTest extends IntegrationTest {

    private final Member member1 = new Member(1L, "user1@email.com", "user1Password", 5_000);
    private final Member member2 = new Member(2L, "user2@email.com", "user2Password", 2_000);
    private final Product product1 = new Product(1L, "product1", 10_000, "product1ImageUrl", 100);
    private final Product product2 = new Product(2L, "product2", 20_000, "product2ImageUrl", 200);
    private final CartItem cartItem1 = new CartItem(1L, member1, product1, new Quantity(1));
    private final CartItem cartItem2 = new CartItem(2L, member1, product2, new Quantity(1));
    private final CartItem cartItem3 = new CartItem(3L, member2, product1, new Quantity(1));
    private final CartItem cartItem4 = new CartItem(4L, member2, product2, new Quantity(1));

    private ProductInfoRequest productInfoRequest1;
    private ProductInfoRequest productInfoRequest2;
    private CartItemInfoRequest cartItemInfoRequest1;
    private CartItemInfoRequest cartItemInfoRequest2;

    private List<CartItemInfoRequest> cartItemsRequest;
    private int totalProductPrice;
    private int totalDeliveryFee;
    private int usePoint;
    private int totalPrice;

    private OrderRequest orderRequest;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        this.productInfoRequest1 = new ProductInfoRequest(product1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl(), product1.getStock());
        this.productInfoRequest2 = new ProductInfoRequest(product2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl(), product2.getStock());

        this.cartItemInfoRequest1 = new CartItemInfoRequest(cartItem1.getId(), cartItem1.getQuantityValue(), productInfoRequest1);
        this.cartItemInfoRequest2 = new CartItemInfoRequest(cartItem2.getId(), cartItem2.getQuantityValue(), productInfoRequest2);

        this.cartItemsRequest = List.of(cartItemInfoRequest1, cartItemInfoRequest2);
        this.totalProductPrice = productInfoRequest1.getPrice() + productInfoRequest2.getPrice();
        this.totalDeliveryFee = 3_000;
        this.usePoint = 0;
        this.totalPrice = this.totalProductPrice + this.totalDeliveryFee - usePoint;

        this.orderRequest = new OrderRequest(cartItemsRequest, totalProductPrice, totalDeliveryFee, usePoint, totalPrice);

        setDummyData();
    }

    private void setDummyData() {
        memberRepository.save(member1);
        memberRepository.save(member2);
        productDao.insert(product1);
        productDao.insert(product2);
        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);
        cartItemRepository.save(cartItem3);
        cartItemRepository.save(cartItem4);
    }

    @Test
    void 주문을_성공적으로_수행한다() {
        // when
        var response = given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/orders/" + 1);
    }

    private ExtractableResponse<Response> orderRequest(final Member member, final OrderRequest orderRequest) {
        return given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .extract();
    }

    @Test
    void 주문이_성공적으로_이뤄지면_총_상품_가격에서_5퍼센트가_포인트로_적립된다() {
        // given
        orderRequest(member1, orderRequest);

        // when
        int result = memberRepository.findById(member1.getId()).getPointValue();

        // then
        assertThat(result).isEqualTo(member1.getPointValue() + (int) (orderRequest.getTotalProductPrice() * 0.05));
    }

    @Test
    void 상품의_잔여_수량보다_주문_수량이_많으면_예외를_발생한다() {
        // given
        CartItem cartItem = new CartItem(1L, member1, product1, new Quantity(9999));
        CartItemInfoRequest cartItemInfoRequest = new CartItemInfoRequest(cartItem.getId(), cartItem.getQuantityValue(), productInfoRequest1);
        OrderRequest orderRequest = new OrderRequest(List.of(cartItemInfoRequest), totalProductPrice, totalDeliveryFee, usePoint, totalPrice);

        // when
        var response = orderRequest(member1, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 다른_사용자의_주문이_이루어지는_경우_예외를_발생한다() {
        // when
        var response = orderRequest(member2, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 요청에_담긴_총_상품_금액과_실제_DB에_저장된_상품들의_총_금액이_다르면_예외가_발생한다() {
        // given
        Product newProduct = new Product(product1.getId(), product1.getName(), product1.getPrice() + 9999, product1.getImageUrl(), product1.getStock());
        productDao.update(newProduct);

        // when
        var response = orderRequest(member1, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 사용한_포인트가_최소_사용_포인트_미만이면_예외를_발생한다() {
        // given
        OrderRequest orderRequest = new OrderRequest(cartItemsRequest, totalProductPrice, totalDeliveryFee, 2999, totalPrice);

        // when
        var response = orderRequest(member1, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 특정_회원의_전체_주문_목록을_조회한다() {
        // given
        orderRequest(member1, orderRequest);

        CartItemInfoRequest cartItemInfoRequest3 = new CartItemInfoRequest(cartItem3.getId(), cartItem3.getQuantityValue(), productInfoRequest1);
        CartItemInfoRequest cartItemInfoRequest4 = new CartItemInfoRequest(cartItem4.getId(), cartItem4.getQuantityValue(), productInfoRequest2);
        OrderRequest orderRequest2 = new OrderRequest(List.of(cartItemInfoRequest3, cartItemInfoRequest4), totalProductPrice, totalDeliveryFee, usePoint, totalPrice);
        orderRequest(member2, orderRequest2);

        // when
        var response = given()
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .when()
                .get("/orders")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
    }

    @Test
    void 특정_주문에_대한_상세_정보를_조회한다() {
        // given
        orderRequest(member1, orderRequest);

        // when
        var response = when()
                .get("/orders/{orderId}", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
    }
}
