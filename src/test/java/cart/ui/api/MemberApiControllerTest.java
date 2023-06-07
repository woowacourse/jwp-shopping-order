package cart.ui.api;

import cart.application.MemberService;
import cart.dao.MemberDao;
import cart.domain.MemberGrade;
import cart.dto.MemberResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(MemberApiController.class)
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberDao memberDao;
    @MockBean
    private MemberService memberService;

    @DisplayName("모든 회원을 조회할 수 있다")
    @Test
    void findAll() throws Exception {
        when(memberService.findAll())
                .thenReturn(List.of(new MemberResponse(1L, "salmon@naver.com", "1234", MemberGrade.GOLD)));

        mockMvc.perform(MockMvcRequestBuilders.get("/members"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email", Matchers.is("salmon@naver.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].password", Matchers.is("1234")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].grade", Matchers.is("GOLD")));
    }
}
