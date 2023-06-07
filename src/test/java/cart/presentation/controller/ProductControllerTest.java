package cart.presentation.controller;

import cart.WebMvcConfig;
import cart.application.service.ProductService;
import cart.presentation.dto.request.CartItemRequest;
import cart.presentation.dto.request.ProductRequest;
import cart.presentation.dto.response.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class, HandlerMethodArgumentResolver.class}))
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("모든 상품을 조회할 수 있다")
    void getAllProducts() throws Exception {
        // given
        when(productService.getAllProducts()).thenReturn(List.of(
                new ProductResponse(1L, "피자", 1000L, "https://", 10.0, false)));
        // when
        mockMvc.perform(get("/products"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].price").exists())
                .andExpect(jsonPath("$[*].imageUrl").exists())
                .andExpect(jsonPath("$[*].pointRatio").exists())
                .andExpect(jsonPath("$[*].pointAvailable").exists());
    }

    @Test
    @DisplayName("특정 상품을 조회할 수 있다")
    void getProductById() throws Exception {
        // given
        when(productService.getProductById(any())).thenReturn(
                new ProductResponse(1L, "피자", 1000L, "https://", 10.0, false));
        // when
        mockMvc.perform(get("/products/{id}", 1))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.imageUrl").exists())
                .andExpect(jsonPath("$.pointRatio").exists())
                .andExpect(jsonPath("$.pointAvailable").exists());
    }

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void createProduct() throws Exception {
        // given
        ProductRequest request = new ProductRequest("피자", 1000L, "", 5.0, false);
        String body = objectMapper.writeValueAsString(request);
        when(productService.createProduct(any())).thenReturn(1L);
        // when
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                // then
                .andExpect(header().string("Location", "/products/1"))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("상품을 업데이트 할 수 있다")
    void updateProduct() throws Exception {
        // given
        ProductRequest request = new ProductRequest("피자", 1000L, "", 5.0, false);
        String body = objectMapper.writeValueAsString(request);
        doNothing().when(productService).updateProduct(any(), any());
        // when
        mockMvc.perform(put("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void deleteProduct() throws Exception {
        // given
        doNothing().when(productService).deleteProduct(any());
        // when
        mockMvc.perform(delete("/products/{id}", 1))
                // then
                .andExpect(status().isNoContent());
    }
}
