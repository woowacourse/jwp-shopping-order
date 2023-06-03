package cart.controller.coupon;

import cart.config.auth.guard.order.MemberOrderArgumentResolver;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.dto.coupon.CouponCreateRequest;
import cart.dto.coupon.CouponResponse;
import cart.repository.coupon.CouponRepository;
import cart.repository.coupon.MemberCouponRepository;
import cart.repository.member.MemberRepository;
import cart.service.coupon.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.CouponFixture.createCoupons;
import static cart.helper.RestDocsHelper.customDocument;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CouponController.class)
@AutoConfigureRestDocs
class CouponControllerUnitTest {

    @MockBean
    private CouponService couponService;

    @MockBean
    private MemberOrderArgumentResolver memberArgumentResolver;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CouponRepository couponRepository;

    @MockBean
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("모든 쿠폰을 조회한다.")
    @Test
    void find_all_coupons() throws Exception {
        // given
        Coupons coupons = createCoupons();
        List<CouponResponse> response = coupons.getCoupons().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        when(couponService.findAllCoupons()).thenReturn(response);

        // when & then
        mockMvc.perform(get("/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].couponId").value(1))
                .andExpect(jsonPath("$[0].couponName").value("1000원 할인 쿠폰"))
                .andExpect(jsonPath("$[1].couponId").value(2))
                .andExpect(jsonPath("$[1].couponName").value("10% 할인 쿠폰"))
                .andDo(customDocument("find_all_coupons",
                        responseFields(
                                fieldWithPath("[0].couponId").description("쿠폰의 id"),
                                fieldWithPath("[0].couponName").description("쿠폰의 이름"),
                                fieldWithPath("[1].couponId").description("쿠폰의 id"),
                                fieldWithPath("[1].couponName").description("쿠폰의 이름")
                        )
                ));
    }

    @DisplayName("id를 기준으로 쿠폰을 조회한다.")
    @Test
    void find_coupon_by_id() throws Exception {
        // given
        Coupon coupon = createCoupons().getCoupons().get(0);
        long couponId = 1;
        when(couponService.findById(couponId)).thenReturn(CouponResponse.from(coupon));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/coupons/{id}", couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponId").value(1))
                .andExpect(jsonPath("$.couponName").value("1000원 할인 쿠폰"))
                .andDo(customDocument("find_coupon_by_id",
                        pathParameters(
                                parameterWithName("id").description("쿠폰의 id")
                        ),
                        responseFields(
                                fieldWithPath(".couponId").description("쿠폰의 id"),
                                fieldWithPath(".couponName").description("쿠폰의 이름")
                        )
                ));
    }

    @DisplayName("쿠폰을 생성한다.")
    @Test
    void create_coupon() throws Exception {
        // given
        CouponCreateRequest req = new CouponCreateRequest("쿠폰", true, 10);
        when(couponService.createCoupon(req)).thenReturn(1L);

        // when & then
        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/coupons/0"))
                .andDo(customDocument("create-coupon",
                        requestFields(
                                fieldWithPath("name").description("쿠폰 이름"),
                                fieldWithPath("isPercentage").description("true"),
                                fieldWithPath("amount").description("10")
                        )));
    }

    @DisplayName("쿠폰을 삭제한다.")
    @Test
    void delete_coupon_by_id() throws Exception {
        // given
        long id = 1L;

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/coupons/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(customDocument("delete_coupon",
                        pathParameters(
                                parameterWithName("id").description("쿠폰의 id")
                        )
                ));
    }

    @DisplayName("멤버에게 쿠폰을 준다.")
    @Test
    void give_coupon_to_member() throws Exception {
        // given
        long couponId = 1;
        long memberId = 1;

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/coupons/{couponId}/members/{memberId}", couponId, memberId))
                .andExpect(status().isOk())
                .andDo(customDocument("give_coupon",
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰의 id"),
                                parameterWithName("memberId").description("멤버의 id")
                        )));
    }
}
