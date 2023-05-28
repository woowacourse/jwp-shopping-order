package cart.ui;

import cart.dao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class MemberArgumentResolverTest {

    @SpyBean
    MemberAuthExtractor memberAuthExtractor;

    @MockBean
    CartItemApiController cartItemApiController;

    @MockBean
    ProductApiController productApiController;

    @MockBean
    PageController pageController;

    @MockBean
    MemberDao memberDao;

    @Autowired
    MockMvc mockMvc;

    @Test
    void Authorization헤더가_존재하지_않으면_401_응답한다() throws Exception {
        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("로그인이 필요한 서비스입니다"));
    }
}
