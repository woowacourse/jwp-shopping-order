package cart.ui;

import cart.dao.MemberDao;
import cart.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductControllerTest.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    private MemberService memberService;


    @DisplayName("PathVariable 검증 - GET 요청에서 ID가 null 이면 예외 발생")
    @Test
    public void validatePathVariable() throws Exception {
        String invalidId = null;

        mockMvc.perform(get("/products/{id}", invalidId))  // "/your-endpoint/{id}"를 실제 엔드포인트로 교체해주세요.
                .andExpect(status().isNotFound());
    }
}
