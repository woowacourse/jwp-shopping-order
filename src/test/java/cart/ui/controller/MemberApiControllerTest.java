package cart.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.test.ControllerTest;
import cart.ui.controller.dto.response.MemberPointResponse;
import cart.ui.controller.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

@WebMvcTest(MemberApiController.class)
class MemberApiControllerTest extends ControllerTest {

    @Autowired
    private MemberService memberService;

    @Nested
    @DisplayName("getMemberPoint 메서드는 ")
    class GetMemberPoint {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            mockMvc.perform(get("/members/point"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @Test
        @DisplayName("유효한 요청이라면 멤버 포인트를 응답한다.")
        void getMemberPoint() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 100);
            MemberPointResponse response = new MemberPointResponse(100);
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
            given(memberService.getMemberPoint(any(Member.class))).willReturn(response);

            MvcResult mvcResult = mockMvc.perform(get("/members/point")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = mvcResult.getResponse().getContentAsString();
            MemberPointResponse result = objectMapper.readValue(jsonResponse, MemberPointResponse.class);
            assertThat(result).usingRecursiveComparison().isEqualTo(response);
        }
    }
}
