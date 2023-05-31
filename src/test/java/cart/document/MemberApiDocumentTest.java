package cart.document;

import cart.WebMvcConfig;
import cart.application.MemberService;
import cart.dao.MemberDao;
import cart.dto.WithdrawRequest;
import cart.ui.MemberApiController;
import cart.ui.MemberArgumentResolver;
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

import static cart.fixtures.MemberFixtures.MemberA;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(MemberApiDocumentTest.class)
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
                .setCustomArgumentResolvers(new MemberArgumentResolver(memberDao))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void 포인트_충전_문서화() throws Exception {
        // given
        given(memberDao.getMemberByEmail(MemberA.EMAIL)).willReturn(MemberA.ENTITY);
        given(memberService.depositPoint(MemberA.ENTITY, 5000L))
                .willReturn(15000L);
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());
        final WithdrawRequest request = new WithdrawRequest(5000L);

        // when, then
        mockMvc.perform(post("/members/cash")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("members/cash",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("[Basic Auth] 로그인 정보")
                                ),
                                requestFields(
                                        fieldWithPath("point").type(JsonFieldType.NUMBER).description("충전할 금액")
                                ),
                                responseFields(
                                        fieldWithPath("cash").type(JsonFieldType.NUMBER).description("충전한 이후의 총 금액"))
                        )
                );
    }

}
