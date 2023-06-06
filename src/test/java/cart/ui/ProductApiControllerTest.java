package cart.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.exception.ProductNotFoundException;
import cart.fixture.ProductRequestFixture;
import cart.fixture.ProductStockResponseFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private MemberDao memberDao;

    @Test
    void createProduct() throws Exception {
        final String request = objectMapper.writeValueAsString(ProductRequestFixture.CHICKEN);
        given(productService.createProduct(any())).willReturn(ProductStockResponseFixture.CHICKEN);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("치킨")))
                .andExpect(jsonPath("$.price", is(10000)))
                .andExpect(jsonPath("$.imageUrl", is("http://example.com/chicken.jpg")));
    }

    @Test
    void getAllProducts() throws Exception {
        given(productService.getAllProductStockResponses()).willReturn(List.of(
                ProductStockResponseFixture.CHICKEN,
                ProductStockResponseFixture.PIZZA
        ));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("치킨")))
                .andExpect(jsonPath("$[0].price", is(10000)))
                .andExpect(jsonPath("$[0].imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$[0].stock", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("피자")))
                .andExpect(jsonPath("$[1].price", is(15000)))
                .andExpect(jsonPath("$[1].imageUrl", is("http://example.com/pizza.jpg")))
                .andExpect(jsonPath("$[1].stock", is(10)));
    }

    @Test
    void getProductById() throws Exception {
        given(productService.getProductStockResponseById(anyLong())).willReturn(ProductStockResponseFixture.CHICKEN);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("치킨")))
                .andExpect(jsonPath("$.price", is(10000)))
                .andExpect(jsonPath("$.imageUrl", is("http://example.com/chicken.jpg")));
    }

    @DisplayName("존재하지 않는 상품을 조회하면 badRequest 를 응답한다.")
    @Test
    void getProductByNoExistId() throws Exception {
        given(productService.getProductStockResponseById(anyLong())).willThrow(new ProductNotFoundException(1L));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("id: 1 에 해당하는 상품을 찾을 수 없습니다.")));
    }

    @Test
    void updateProduct() throws Exception {
        final String request = objectMapper.writeValueAsString(ProductRequestFixture.CHICKEN);
        given(productService.updateProduct(anyLong(), any())).willReturn(ProductStockResponseFixture.CHICKEN);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("치킨")))
                .andExpect(jsonPath("$.price", is(10000)))
                .andExpect(jsonPath("$.imageUrl", is("http://example.com/chicken.jpg")));
    }

    @DisplayName("존재하지 않는 상품을 업데이트하면 badRequest 를 응답한다.")
    @Test
    void updateProductWithNoExistId() throws Exception {
        final String request = objectMapper.writeValueAsString(ProductRequestFixture.CHICKEN);
        given(productService.updateProduct(anyLong(), any())).willThrow(new ProductNotFoundException(1L));

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("id: 1 에 해당하는 상품을 찾을 수 없습니다.")));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}
