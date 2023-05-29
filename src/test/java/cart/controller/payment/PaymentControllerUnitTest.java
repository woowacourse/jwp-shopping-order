package cart.controller.payment;

import cart.config.auth.guard.order.MemberOrderArgumentResolver;
import cart.config.web.WebMvcConfig;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.dto.payment.PaymentResponse;
import cart.service.payment.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private WebMvcConfig webMvcConfig;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Member member;

    @BeforeEach
    void init() throws Exception {
        member = createMember();
        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
    }

    @DisplayName("구매 페이지를 조회한다.")
    @Test
    void find_payment_page() throws Exception {
        // given
        Coupons coupons = createCoupons();
        member.initCoupons(coupons);

        PaymentResponse paymentResponse = PaymentResponse.from(member, createSimpleCart());
        when(paymentService.findPaymentPage(any(Member.class))).thenReturn(paymentResponse);

        // when & then
        mockMvc.perform(get("/payments")
                        .header("Authorization", "member")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("치킨"))
                .andExpect(jsonPath("$[0].price").value(20000))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[0].imgUrl").value("img.img"))
                .andExpect(jsonPath("$[0].isOnSale").value("false"))
                .andExpect(jsonPath("$[0].salePrice").value(0))
                .andDo(customDocument("find_all_products",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("[0].productId").description(1L),
                                fieldWithPath("[0].productName").description("치킨"),
                                fieldWithPath("[0].price").description(10000),
                                fieldWithPath("[0].quantity").description(10),
                                fieldWithPath("[0].imgUrl").description("img"),
                                fieldWithPath("[0].isOnSale").description(false),
                                fieldWithPath("[0].salePrice").description(100)
                        )
                ));
    }

}
