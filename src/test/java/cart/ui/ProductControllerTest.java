package cart.ui;

import cart.dao.MemberDao;
import cart.dto.ProductRequest;
import cart.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    private ProductService productService;


    @DisplayName("PathVariable 검증 - GET 요청에서 ID가 1 미만 이면 예외 발생")
    @Test
    public void validatePathVariable_Get() throws Exception {
        Long invalidId = 0L;
        String expectedResponse = "[\"ID는 1 이상의 정수로 입력해주세요\"]";

        mockMvc.perform(get("/products/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
    }

    @DisplayName("PathVariable 검증 - PUT 요청에서 ID가 1 미만이면 예외 발생")
    @Test
    public void validatePathVariable_Put() throws Exception {
        Long invalidId = 0L;
        ProductRequest productRequest = new ProductRequest("치킨", 10, "http://naver.com");
        String expectedResponse = "[\"ID는 1 이상의 정수로 입력해주세요\"]";

        mockMvc.perform(put("/products/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
    }

    @DisplayName("PathVariable 검증 - DELETE 요청에서 ID가 1 미만이면 예외 발생")
    @Test
    public void validatePathVariable_Delete() throws Exception {
        Long invalidId = 0L;
        String expectedResponse = "[\"ID는 1 이상의 정수로 입력해주세요\"]";

        mockMvc.perform(delete("/products/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
    }
}
