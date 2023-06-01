package cart.controller.member;

import cart.config.auth.guard.order.MemberOrderArgumentResolver;
import cart.dto.member.MemberCreateRequest;
import cart.dto.member.MemberResponse;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import cart.service.member.MemberService;
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

import static cart.helper.RestDocsHelper.customDocument;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
class MemberControllerUnitTest {

    @MockBean
    private MemberService memberService;

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

    @DisplayName("멤버를 생성한다.")
    @Test
    void create_member() throws Exception {
        // given
        MemberCreateRequest req = new MemberCreateRequest("a@a.com", "1234");

        // when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                ).andExpect(status().isOk())
                .andDo(customDocument("create_member",
                        requestFields(
                                fieldWithPath("email").description("email@email.com"),
                                fieldWithPath("password").description("1234")
                        )
                ));
    }

    @DisplayName("멤버를 전체 조회한다.")
    @Test
    void find_all_members() throws Exception {
        // given
        List<MemberResponse> memberResponses = List.of(new MemberResponse(1, "a@a.com", "1234"));
        when(memberService.findAll()).thenReturn(memberResponses);

        // when & then
        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andDo(customDocument("find_all_members",
                        responseFields(
                                fieldWithPath("[0].memberId").description(1),
                                fieldWithPath("[0].email").description("a@a.com"),
                                fieldWithPath("[0].password").description("1234")
                        )));
    }

    @DisplayName("멤버를 개별 조회한다.")
    @Test
    void find_member() throws Exception {
        // given
        MemberResponse response = new MemberResponse(1, "a@a.com", "1234");
        when(memberService.findById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/members/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(customDocument("find_member",
                        pathParameters(
                                parameterWithName("id").description("member_id")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description(1),
                                fieldWithPath("email").description("a@a.com"),
                                fieldWithPath("password").description("1234")
                        )));
    }

    @DisplayName("멤버를 삭제한다.")
    @Test
    void delete_member() throws Exception {
        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/members/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(customDocument("delete_member",
                        pathParameters(
                                parameterWithName("id").description("member_id")
                        )
                ));
    }
}
