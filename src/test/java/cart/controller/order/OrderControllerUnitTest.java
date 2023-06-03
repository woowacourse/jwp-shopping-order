package cart.controller.order;

import cart.config.auth.guard.basic.MemberArgumentResolver;
import cart.domain.member.Member;
import cart.dto.history.OrderHistory;
import cart.dto.history.OrderedCouponHistory;
import cart.dto.history.OrderedProductHistory;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import cart.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.fixture.CouponFixture.createCoupons;
import static cart.fixture.MemberFixture.createMember;
import static cart.helper.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureRestDocs
public class OrderControllerUnitTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberArgumentResolver memberArgumentResolver;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CouponRepository couponRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        Member member = createMember();
        member.initCoupons(createCoupons());

        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberRepository.findByEmail(any())).willReturn(member);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
    }

    @DisplayName("모든 주문 내역을 조회한다.")
    @Test
    void find_all_orders() throws Exception {
        // given
        OrderHistory orderHistory = new OrderHistory(
                1L,
                List.of(new OrderedProductHistory(1L, "치킨", "img", 10, 10000)),
                3000,
                List.of(new OrderedCouponHistory(1, "coupon")),
                "2022-05-29"
        );

        OrdersResponse ordersResponse = OrdersResponse.from(List.of(orderHistory));
        when(orderService.findOrders(any(Member.class))).thenReturn(ordersResponse);

        // when & then
        mockMvc.perform(get("/orders")
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value(1))
                .andExpect(jsonPath("$.orders[0].orderedTime").value("2022-05-29"))
                .andExpect(jsonPath("$.orders[0].products[0].productId").value(1))
                .andExpect(jsonPath("$.orders[0].products[0].productName").value("치킨"))
                .andExpect(jsonPath("$.orders[0].products[0].imgUrl").value("img"))
                .andExpect(jsonPath("$.orders[0].products[0].quantity").value(10))
                .andExpect(jsonPath("$.orders[0].products[0].price").value(10000))
                .andExpect(jsonPath("$.orders[0].deliveryPrice.deliveryPrice").value(3000))
                .andExpect(jsonPath("$.orders[0].coupons[0].couponId").value(1))
                .andExpect(jsonPath("$.orders[0].coupons[0].couponName").value("coupon"))
                .andDo(customDocument("find_orders",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("orders[0].orderId").description("주문 내역의 id"),
                                fieldWithPath("orders[0].orderedTime").description("주문 시간"),
                                fieldWithPath("orders[0].products[0].productId").description("상품의 id"),
                                fieldWithPath("orders[0].products[0].productName").description("상품의 이름"),
                                fieldWithPath("orders[0].products[0].imgUrl").description("상품의 이미지 주소"),
                                fieldWithPath("orders[0].products[0].quantity").description("상품의 수량"),
                                fieldWithPath("orders[0].products[0].price").description("상품의 가격"),
                                fieldWithPath("orders[0].deliveryPrice.deliveryPrice").description("상품의 배달 가격"),
                                fieldWithPath("orders[0].coupons[0].couponId").description("쿠폰의 id"),
                                fieldWithPath("orders[0].coupons[0].couponName").description("쿠폰의 이름")
                        )
                ));
    }

    @DisplayName("주문 내역을 단건 조회한다.")
    @Test
    void find_order() throws Exception {
        // given

        OrderHistory orderHistory = new OrderHistory(
                1L,
                List.of(new OrderedProductHistory(1L, "치킨", "img", 10, 10000)),
                3000,
                List.of(new OrderedCouponHistory(1, "coupon")),
                "2022-05-29"
        );

        when(orderService.findOrder(any(Member.class), anyLong())).thenReturn(OrderResponse.from(orderHistory));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/orders/{id}", 1L)
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.orderedTime").value("2022-05-29"))
                .andExpect(jsonPath("$.products[0].productId").value(1))
                .andExpect(jsonPath("$.products[0].productName").value("치킨"))
                .andExpect(jsonPath("$.products[0].imgUrl").value("img"))
                .andExpect(jsonPath("$.products[0].quantity").value(10))
                .andExpect(jsonPath("$.products[0].price").value(10000))
                .andExpect(jsonPath("$.deliveryPrice.deliveryPrice").value(3000))
                .andExpect(jsonPath("$.coupons[0].couponId").value(1))
                .andExpect(jsonPath("$.coupons[0].couponName").value("coupon"))
                .andDo(customDocument("find_order",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        pathParameters(
                                parameterWithName("id").description("product_id")
                        ),
                        responseFields(
                                fieldWithPath("orderId").description("주문 내역의 id"),
                                fieldWithPath("orderedTime").description("주문 시간"),
                                fieldWithPath("products[0].productId").description("상품의 id"),
                                fieldWithPath("products[0].productName").description("상품의 이름"),
                                fieldWithPath("products[0].imgUrl").description("상품의 이미지 주소"),
                                fieldWithPath("products[0].quantity").description("상품의 수량"),
                                fieldWithPath("products[0].price").description("상품의 가격"),
                                fieldWithPath("deliveryPrice.deliveryPrice").description("상품의 배달료"),
                                fieldWithPath("coupons[0].couponId").description("쿠폰의 id"),
                                fieldWithPath("coupons[0].couponName").description("쿠폰의 이름")
                        )
                ));
    }
}
