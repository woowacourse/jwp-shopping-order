package cart.ui;

import static cart.fixture.TestFixture.AUTHORIZATION_HEADER_MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_FIXED_2000;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_PERCENTAGE_50;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.CouponService;
import cart.controller.docs.ControllerTestWithDocs;
import cart.dao.MemberDao;
import cart.domain.MemberCoupon;
import cart.dto.MemberCouponResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(CouponController.class)
class CouponControllerTest extends ControllerTestWithDocs {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    CouponService couponService;

    @BeforeEach
    void setUp() {
        when(memberDao.getMemberByEmail(anyString())).thenReturn(Optional.empty());
        when(memberDao.getMemberByEmail(eq(MEMBER_A.getEmail()))).thenReturn(Optional.of(MEMBER_A));
    }

    @Test
    void 사용자의_모든_쿠폰을_조회한다() throws Exception {
        List<MemberCoupon> memberCoupons = List.of(MEMBER_A_COUPON_FIXED_2000(), MEMBER_A_COUPON_PERCENTAGE_50());
        when(couponService.getMemberCouponsOf(eq(MEMBER_A))).thenReturn(memberCoupons);
        List<MemberCouponResponse> responses = MemberCouponResponse.of(memberCoupons);

        ResultActions result = mockMvc.perform(get("/coupons")
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_MEMBER_A)
        );

        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentationOf(
                        responses,
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }
}
