package cart.ui;

import cart.dao.MemberDao;
import cart.dto.ProductRequest;
import cart.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void validatePathVariable_Get() throws Exception {
        String invalidId = null;

        mockMvc.perform(get("/products/{id}", invalidId))
                .andExpect(status().isNotFound());
    }

    @DisplayName("PathVariable 검증 - PUT 요청에서 ID가 null 이면 예외 발생")
    @Test
    public void validatePathVariable_Put() throws Exception {
        String invalidId = null;
        ProductRequest productRequest = new ProductRequest("치킨", 10, "http://naver.com");

        mockMvc.perform(put("/products/{id}", invalidId)
                        .content(new ObjectMapper().writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("PathVariable 검증 - DELETE 요청에서 ID가 null 이면 예외 발생")
    @Test
    public void validatePathVariable_Delete() throws Exception {
        String invalidId = null;

        mockMvc.perform(delete("/products/{id}", invalidId))
                .andExpect(status().isNotFound());
    }
}
