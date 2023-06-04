package cart.ui;

import static cart.helper.RestDocsHelper.prettyDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dao.MemberDao;
import cart.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberDao memberDao;

    @Test
    void 회원의_포인트를_조회한다() throws Exception {
        Member member = new Member(1L, "aa@aaa.com", "1234", 1000);
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(get("/members/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0"))
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
