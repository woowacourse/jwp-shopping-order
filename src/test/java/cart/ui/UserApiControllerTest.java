package cart.ui;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cart.application.UserService;
import cart.dao.MemberDao;
import cart.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private MemberDao memberDao;

    @Test
    void getUser() throws Exception {
        given(userService.getUserResponse(any())).willReturn(new UserResponse("odo1@woowa.com", 1000, 5));

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$.email", is("odo1@woowa.com")))
                .andExpect(jsonPath("$.point", is(1000)))
                .andExpect(jsonPath("$.earnRate", is(5.0)));
    }
}
