package cart.integration;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.memberCoupon.MemberCoupon;
import cart.domain.memberCoupon.MemberCouponRepository;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.orderProduct.OrderProduct;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.dto.CouponResponse;
import cart.dto.ProductResponse;
import cart.dto.order.*;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    private Member member;
    private Product product;
    private Product product2;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberRepository.findById(1L);
        product = productRepository.findById(1L);
        product2 = productRepository.findById(2L);
        coupon = couponRepository.findById(1L);
    }

    @Test
    void 주문을_추가한다() {
        long memberCouponId = memberCouponRepository.add(new MemberCoupon(member, coupon));
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), memberCouponId);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 멤버가_지니고_있지_않은_쿠폰을_구매시에_사용하면_실패한다() {
        initMemberCoupons(member);
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1L);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 멤버의_전체_주문을_조회한다() {
        OrderProduct orderProduct = OrderProduct.of(product, 1);
        OrderProduct orderProduct2 = OrderProduct.of(product2, 2);
        Order order = new Order(1000, 1000, List.of(orderProduct), member);
        Order order2 = new Order(2000, 2000, List.of(orderProduct2), member);
        orderRepository.add(order2);
        orderRepository.add(order);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);
        OrderResponse orderResponse = orderResponses.get(0);
        OrderResponse orderResponse2 = orderResponses.get(1);

        assertThat(orderResponse.getConfirmState()).isFalse();
        assertThat(orderResponse2.getConfirmState()).isFalse();

        OrderProductResponse orderProductResponse = orderResponse.getOrderProducts().get(0);
        OrderProductResponse orderProductResponse2 = orderResponse2.getOrderProducts().get(0);

        assertThat(orderProductResponse.getQuantity()).isEqualTo(1);
        assertThat(orderProductResponse2.getQuantity()).isEqualTo(2);

        assertThat(orderProductResponse.getProduct()).usingRecursiveComparison()
                .isEqualTo(ProductResponse.from(product));
        assertThat(orderProductResponse2.getProduct()).usingRecursiveComparison()
                .isEqualTo(ProductResponse.from(product2));
    }

    @Test
    void 멤버의_주문을_상세_조회한다() {
        OrderProduct orderProduct = OrderProduct.of(product, 1);
        Order order = new Order(2000, 1000, List.of(orderProduct), coupon, member);
        Long orderId = orderRepository.add(order);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .log().all()
                .extract();

        OrderDetailResponse orderDetailResponse = response.jsonPath().getObject(".", OrderDetailResponse.class);
        assertThat(orderDetailResponse.getId()).isEqualTo(orderId);
        assertThat(orderDetailResponse.getOriginalPrice()).isEqualTo(2000);
        assertThat(orderDetailResponse.getDiscountPrice()).isEqualTo(1000);
        assertThat(orderDetailResponse.getConfirmState()).isFalse();
        assertThat(orderDetailResponse.getCoupon()).usingRecursiveComparison().isEqualTo(CouponResponse.from(coupon));

        ProductResponse productResponse = ProductResponse.from(product);
        assertThat(orderDetailResponse.getOrderProducts().get(0)).usingRecursiveComparison()
                .isEqualTo(new OrderProductResponse(productResponse, 1));

    }

    @Test
    void 주문을_확정한다() {
        OrderProduct orderProduct = OrderProduct.of(product, 1);
        Order order = new Order(2000, 1000, List.of(orderProduct), coupon, member);
        Long orderId = orderRepository.add(order);
        Coupon bonusCoupon = coupon;

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .patch("/orders/" + orderId + "/confirm")
                .then()
                .log().all()
                .extract();

        OrderConfirmResponse orderConfirmResponse = response.jsonPath().getObject(".", OrderConfirmResponse.class);
        assertThat(orderConfirmResponse.getCoupon()).usingRecursiveComparison()
                .isEqualTo(CouponResponse.from(bonusCoupon));
    }

    @Test
    void 주문을_삭제한다() {
        OrderProduct orderProduct = OrderProduct.of(product, 1);
        Order order = new Order(2000, 1000, List.of(orderProduct), coupon, member);
        Long orderId = orderRepository.add(order);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/orders/" + orderId)
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 확정한_주문을_삭제하려고하면_실패한다(){
        OrderProduct orderProduct = OrderProduct.of(product, 1);
        Order order = new Order(2000, 1000, List.of(orderProduct), coupon, member);
        Long orderId = orderRepository.add(order);
        order = orderRepository.findOrderById(orderId);
        order.confirm();
        orderRepository.update(order);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/orders/" + orderId)
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void initMemberCoupons(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findMemberCouponsByMemberId(member.getId());
        memberCoupons.forEach(memberCoupon -> memberCouponRepository.delete(memberCoupon.getId()));
    }
}
