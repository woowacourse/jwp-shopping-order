package cart.controller;

import cart.application.PointService;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.PointResponse;
import cart.ui.PointApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static cart.ShoppingOrderFixture.member1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PointApiController.class)
class PointApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @MockBean
    private MemberDao memberDao;

    @DisplayName("사용자가 가진 포인트를 가져온다")
    @Test
    void getPoint() throws Exception {
        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(member1);
        when(pointService.findPointByMemberId(any())).thenReturn(new PointResponse(1000.0));

        this.mockMvc.perform(get("/points")
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("point").exists());
    }
}
