package cart.controller.payment;

import cart.config.auth.guard.order.MemberOrderArgumentResolver;
import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponsResponse;
import cart.dto.product.ProductIdRequest;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import cart.service.payment.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CartFixture.createSimpleCart;
import static cart.fixture.CouponFixture.createCoupons;
import static cart.fixture.MemberFixture.createMember;
import static cart.helper.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@AutoConfigureRestDocs
class PaymentControllerUnitTest {

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private MemberOrderArgumentResolver memberArgumentResolver;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CouponRepository couponRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    @BeforeEach
    void init() throws Exception {
        member = createMember();
        member.initCoupons(createCoupons());

        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberRepository.findByEmail(any())).willReturn(member);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
    }

    @DisplayName("구매 페이지를 조회한다.")
    @Test
    void find_payment_page() throws Exception {
        // given
        PaymentResponse paymentResponse = PaymentResponse.from(member, createSimpleCart());
        when(paymentService.findPaymentPage(any(Member.class))).thenReturn(paymentResponse);

        // when & then
        mockMvc.perform(get("/payments")
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].productId").value(1L))
                .andExpect(jsonPath("$.products[0].productName").value("치킨"))
                .andExpect(jsonPath("$.products[0].price").value(20000))
                .andExpect(jsonPath("$.products[0].quantity").value(10))
                .andExpect(jsonPath("$.products[0].imgUrl").value("img.img"))
                .andExpect(jsonPath("$.products[0].isOnSale").value("false"))
                .andExpect(jsonPath("$.products[0].salePrice").value(0))
                .andDo(customDocument("find_payment_page",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("products[0].productId").description("상품의 id"),
                                fieldWithPath("products[0].productName").description("상품의 이름"),
                                fieldWithPath("products[0].price").description("상품의 가격"),
                                fieldWithPath("products[0].quantity").description("상품 수량"),
                                fieldWithPath("products[0].imgUrl").description("상품의 이미지 주소"),
                                fieldWithPath("products[0].isOnSale").description("상품의 세일 여부"),
                                fieldWithPath("products[0].salePrice").description("세일되는 가격"),
                                fieldWithPath("coupons[0].couponId").description("쿠폰의 id"),
                                fieldWithPath("coupons[0].couponName").description("쿠폰의 이름"),
                                fieldWithPath("coupons[1].couponId").description("쿠폰의 id"),
                                fieldWithPath("coupons[2].couponName").description("쿠폰의 이름"),
                                fieldWithPath("deliveryPrice.deliveryPrice").description("배달료")
                        )
                ));
    }

    @DisplayName("쿠폰을 적용한다.")
    @Test
    void apply_coupons() throws Exception {
        // given
        List<Long> ids = List.of(1L);
        PaymentUsingCouponsResponse response = PaymentUsingCouponsResponse.from(createCart(), createCoupons().getCoupons());
        when(paymentService.applyCoupons(member, ids)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/payments/coupons")
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                        .param("couponsId", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].productId").value(1))
                .andExpect(jsonPath("$.products[0].originalPrice").value(20000))
                .andExpect(jsonPath("$.products[0].discountPrice").value(2900))
                .andExpect(jsonPath("$.deliveryPrice.originalPrice").value(3000))
                .andExpect(jsonPath("$.deliveryPrice.discountPrice").value(0))
                .andDo(customDocument("apply_coupons",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        requestParameters(
                                parameterWithName("couponsId").description("쿠폰 ID 리스트")
                        ),
                        responseFields(
                                fieldWithPath("products[0].productId").description("1"),
                                fieldWithPath("products[0].originalPrice").description("20000"),
                                fieldWithPath("products[0].discountPrice").description("2900"),
                                fieldWithPath("deliveryPrice.originalPrice").description("3000"),
                                fieldWithPath("deliveryPrice.discountPrice").description("0")
                        )
                ));
    }

    @DisplayName("상품을 결제한다.")
    @Test
    void pay() throws Exception {
        // given
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));

        // when & then
        mockMvc.perform(post("/payments")
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                ).andExpect(status().isCreated())
                .andDo(customDocument("pay",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        requestFields(
                                fieldWithPath("products[0].id").description("1"),
                                fieldWithPath("products[0].quantity").description("10"),
                                fieldWithPath("coupons[0].id").description("1"),
                                fieldWithPath("coupons[1].id").description("2")
                        )));
    }
}
