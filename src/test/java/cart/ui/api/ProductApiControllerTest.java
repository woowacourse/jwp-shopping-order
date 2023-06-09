package cart.ui.api;

import cart.application.ProductService;
import cart.config.WebMvcConfig;
import cart.dto.product.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebMvcConfig.class)})
public class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void 모든_상품_조회성공시_상태코드가_OK_이다() throws Exception {
        // given
        given(productService.getAllProducts())
                .willReturn(Collections.emptyList());

        // when, then
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 상품_아이디로_조회성공시_상태코드가_OK_이다() throws Exception {
        // given
        Long correctProductId = 1L;

        // when, then
        mockMvc.perform(get("/products/" + correctProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 상품_아이디로_조회시_상품_아이디가_음수면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        Long wrongProductId = -1L;

        // when, then
        mockMvc.perform(get("/products/" + wrongProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_추가성공시_상태코드가_CREATED_이다() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("피자", 1000L, "fdsgds", 1000L);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void 상품_추가시_상품_이름이_비어있으면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("", 1000L, "fdsgds", 1000L);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_추가시_상품가격이_음수면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("피자", -1000L, "fdsgds", 1000L);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_추가시_상품_이미지경로가_비어있으면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("피자", 1000L, "", 1000L);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_추가시_상품_재고가_양수가_아니면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("피자", 1000L, "fdsfd", 0L);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_변경성공시_상태코드가_OK_이다() throws Exception {
        // given
        Long id = 3L;
        ProductRequest productRequest = new ProductRequest("피자", 1000L, "fdsfd", 3L);

        // when, then
        mockMvc.perform(put("/products/" + id)
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 상품_삭제성공시_상태코드가_NO_CONTENT_이다() throws Exception {
        // given
        Long id = 3L;

        // when, then
        mockMvc.perform(delete("/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
