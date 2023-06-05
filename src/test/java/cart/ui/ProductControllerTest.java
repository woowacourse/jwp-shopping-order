package cart.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private MemberDao memberDao;

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void findAllProducts() throws Exception {
        final List<ProductResponse> expected = List.of(
                new ProductResponse(1L, "chicken", 10000, "example.com/chicken"),
                new ProductResponse(2L, "pizza", 15000, "example.com/pizza"),
                new ProductResponse(3L, "salad", 14000, "example.com/salad")
        );
        given(productService.findAllProducts()).willReturn(expected);

        final MvcResult mvcResult = mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<ProductResponse> actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("상품 상세 정보를 조회한다.")
    void findProductById() throws Exception {
        final ProductResponse expected = new ProductResponse(1L, "chicken", 10000, "example.com/chicken");
        given(productService.findProductById(eq(1L))).willReturn(expected);

        final MvcResult mvcResult = mockMvc.perform(get("/products/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final ProductResponse actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProductResponse.class
        );
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("상품 정보를 등록한다.")
    void createProduct() throws Exception {
        final ProductRequest request = new ProductRequest("pizza", 30000, "example.com/pizza");
        given(productService.createProduct(any(ProductRequest.class))).willReturn(1L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/products/1"));
    }

    @Test
    @DisplayName("상품 정보를 수정한다.")
    void updateProduct() throws Exception {
        final ProductRequest request = new ProductRequest("pizza", 30000, "example.com/pizza");
        willDoNothing().given(productService).updateProduct(eq(1L), any(ProductRequest.class));

        mockMvc.perform(put("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 정보를 삭제한다.")
    void deleteProduct() throws Exception {
        willDoNothing().given(productService).deleteProduct(eq(1L));

        mockMvc.perform(delete("/products/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("잘못된 상품 정보가 입력될 시 404 응답을 반환한다..")
    void deleteProductWithInvalidId() throws Exception {
        willThrow(new ResourceNotFoundException("잘못된 상품 아이디입니다.")).given(productService).deleteProduct(eq(Long.MAX_VALUE));

        mockMvc.perform(delete("/products/{id}", Long.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("잘못된 상품 아이디입니다."));
    }
}
