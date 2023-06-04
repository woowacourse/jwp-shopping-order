package cart.controller;

import cart.application.CouponService;
import cart.domain.member.Member;
import cart.dto.coupon.CouponCreateRequest;
import cart.fixture.Fixture;
import cart.repository.MemberRepository;
import cart.ui.CouponController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(value = CouponController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CouponApiControllerTest {

    String email = "a@a.com";
    String password = "1234";

    String authString = email + ":" + password;
    String encodedAuthString = new String(Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8)));

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CouponService couponService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 유저_쿠폰_조회() throws Exception {
        //given
        given(couponService.findUnUsedMemberCouponByMember(any(Member.class)))
                .willReturn(List.of(Fixture.멤버_쿠폰, Fixture.멤버_쿠폰2));
        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));

        //when
        ResultActions result = mockMvc.perform(get("/coupons")
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Basic " + encodedAuthString));

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-coupon-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        )
                ));
    }

    @Test
    void 유저_쿠폰_생성() throws Exception {
        //given
        CouponCreateRequest request = new CouponCreateRequest(1L);

        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));

        //when
        ResultActions result = mockMvc.perform(post("/coupon")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Basic " + encodedAuthString));

        //then
        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("member-coupon-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        requestFields(
                                fieldWithPath("id").description("쿠폰 id")
                        )
                ));
    }

}
