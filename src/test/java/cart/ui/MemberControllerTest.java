package cart.ui;

import static cart.helper.RestDocsHelper.prettyDocument;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.MockAuthProviderConfig;
import cart.application.MemberService;
import cart.config.AuthProvider;
import cart.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
@Import(MockAuthProviderConfig.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @Autowired
    AuthProvider authProvider;

    @BeforeEach
    void setUp() {
        given(authProvider.resolveUser(anyString()))
                .willReturn(new User(1L, "a@a.com"));
    }

    @Test
    void 회원의_포인트를_조회한다() throws Exception {
        given(memberService.getMemberPoint(1L))
                .willReturn(1000L);

        mockMvc.perform(get("/members/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "basic YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andDo(prettyDocument(
                        "members/inquiryPoint",
                        relaxedResponseFields(
                                fieldWithPath("result.memberId").description("회원의 ID"),
                                fieldWithPath("result.totalPoint").description("회원의 포인트")
                        )
                ));
    }
}
