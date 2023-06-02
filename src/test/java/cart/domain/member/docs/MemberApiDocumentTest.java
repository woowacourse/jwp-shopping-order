package cart.domain.member.docs;

import static cart.fixtures.MemberFixtures.Dooly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.member.application.MemberService;
import cart.domain.member.dto.MemberCashChargeRequest;
import cart.domain.member.dto.MemberCashChargeResponse;
import cart.domain.member.dto.MemberShowCurrentCashResponse;
import cart.domain.member.persistence.MemberDao;
import cart.domain.member.presentation.MemberApiController;
import cart.global.config.AuthMember;
import cart.global.config.AuthMemberInterceptor;
import cart.global.config.MemberArgumentResolver;
import cart.global.config.WebMvcConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

@AutoConfigureRestDocs
@WebMvcTest(MemberApiController.class)
@ExtendWith(RestDocumentationExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberApiDocumentTest {

    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private MemberService memberService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private MemberDao memberDao;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(new MemberApiController(memberService))
                .addInterceptors(new AuthMemberInterceptor(memberService))
                .setCustomArgumentResolvers(new MemberArgumentResolver())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void 사용자_금액_충전_문서화() throws Exception {
        // given
        given(memberDao.selectMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY());
        MemberCashChargeRequest request = new MemberCashChargeRequest(10000);
        MemberCashChargeResponse response = new MemberCashChargeResponse(15000);
        given(memberService.chargeCash(any(AuthMember.class), any(MemberCashChargeRequest.class)))
                .willReturn(response);
        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(post("/members/cash")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(document("members/chargeCash",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("사용자 인증 정보 (Basic Auth)")
                                ),
                                requestFields(
                                        fieldWithPath("cashToCharge").type(JsonFieldType.NUMBER).description("충전할 금액")
                                ),
                                responseFields(
                                        fieldWithPath("totalCash").type(JsonFieldType.NUMBER).description("충전된 금액")
                                )
                        )
                );
    }

    @Test
    void 사용자_현재_금액_조회_문서화() throws Exception {
        // given
        given(memberDao.selectMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY());
        MemberShowCurrentCashResponse response = new MemberShowCurrentCashResponse(5000);
        given(memberService.findMemberCurrentCharge(any(AuthMember.class)))
                .willReturn(response);
        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/members/cash")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("members/showCurrentCash",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("사용자 인증 정보 (Basic Auth)")
                        ),
                        responseFields(
                                fieldWithPath("totalCash").type(JsonFieldType.NUMBER).description("사용자의 현재 금액")
                        )
                ));
    }
}
