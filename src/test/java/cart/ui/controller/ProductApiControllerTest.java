package cart.ui.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.ProductService;
import cart.domain.product.Product;
import cart.test.ControllerTest;
import cart.ui.controller.dto.request.ProductRequest;
import cart.ui.controller.dto.response.ProductResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("getAllProducts 메서드는 모든 상품 목록을 조회한다.")
    void getAllProducts() throws Exception {
        List<ProductResponse> response = List.of(
                ProductResponse.from(new Product(1L, "치킨", 10000, "http://chicken.com")),
                ProductResponse.from(new Product(2L, "샐러드", 20000, "http://salad.com")),
                ProductResponse.from(new Product(3L, "피자", 13000, "http://pizza.com"))
        );
        given(productService.getAllProducts()).willReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<ProductResponse> result = objectMapper.readValue(jsonResponse,
                new TypeReference<List<ProductResponse>>() {
                }
        );
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Nested
    @DisplayName("getProductById 메서드는 ")
    class GetProductById {

        @ParameterizedTest
        @ValueSource(strings = {"hello", "1.5"})
        @DisplayName("ID가 Long 타입으로 변환할 수 없는 경우 400 상태를 반환한다.")
        void invalidIDType(String id) throws Exception {
            mockMvc.perform(get("/products/{id}", id))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            String.format("올바르지 않은 타입입니다. 필요한 타입은 [%s]입니다. 현재 입력값: %s", Long.class.getSimpleName(), id)));
        }

        @Test
        @DisplayName("ID에 해당하는 상품 정보를 응답한다.")
        void getProductById() throws Exception {
            ProductResponse response = ProductResponse.from(new Product(1L, "치킨", 10000, "http://chicken.com"));
            given(productService.getProductById(anyLong())).willReturn(response);

            MvcResult mvcResult = mockMvc.perform(get("/products/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            String jsonResponse = mvcResult.getResponse().getContentAsString();
            ProductResponse result = objectMapper.readValue(jsonResponse, ProductResponse.class);
            assertThat(result).usingRecursiveComparison().isEqualTo(response);
        }
    }

    @Nested
    @DisplayName("createProduct 메서드는 ")
    class CreateProduct {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("상품 이름이 존재하지 않거나, 비어있으면 400 상태를 반환한다.")
        void emptyProductName(String productName) throws Exception {
            ProductRequest request = new ProductRequest(productName, 10000, "http://product.com");
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/products")
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: name] 상품 이름은 비어있을 수 없습니다.]"));
        }

        @Test
        @DisplayName("상품 가격이 존재하지 않으면 400 상태를 반환한다.")
        void emptyPrice() throws Exception {
            ProductRequest request = new ProductRequest("치킨", null, "http://product.com");
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/products")
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: price] 상품 가격은 존재해야 합니다.]"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("상품 이미지 경로가 존재하지 않거나, 비어있으면 400 상태를 반환한다.")
        void emptyImageUrl(String imageUrl) throws Exception {
            ProductRequest request = new ProductRequest("치킨", 10000, imageUrl);
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/products")
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: imageUrl] 상품 이미지는 비어있을 수 없습니다.]"));
        }

        @Test
        @DisplayName("유효한 요청이라면 상품을 생성한다.")
        void createProduct() throws Exception {
            ProductRequest request = new ProductRequest("치킨", 10000, "http://chicken.com");
            String jsonRequest = objectMapper.writeValueAsString(request);
            given(productService.createProduct(any(ProductRequest.class))).willReturn(1L);

            mockMvc.perform(post("/products")
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/products/" + 1));
        }
    }

    @Nested
    @DisplayName("updateProduct 메서드는 ")
    class UpdateProduct {

        @ParameterizedTest
        @ValueSource(strings = {"hello", "1.5"})
        @DisplayName("ID가 Long 타입으로 변환할 수 없는 경우 400 상태를 반환한다.")
        void invalidIDType(String id) throws Exception {
            mockMvc.perform(put("/products/{id}", id))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            String.format("올바르지 않은 타입입니다. 필요한 타입은 [%s]입니다. 현재 입력값: %s", Long.class.getSimpleName(), id)));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("상품 이름이 존재하지 않거나, 비어있으면 400 상태를 반환한다.")
        void emptyProductName(String productName) throws Exception {
            ProductRequest request = new ProductRequest(productName, 10000, "http://product.com");
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/products/{id}", 1)
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: name] 상품 이름은 비어있을 수 없습니다.]"));
        }

        @Test
        @DisplayName("상품 가격이 존재하지 않으면 400 상태를 반환한다.")
        void emptyPrice() throws Exception {
            ProductRequest request = new ProductRequest("치킨", null, "http://product.com");
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/products/{id}", 1)
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: price] 상품 가격은 존재해야 합니다.]"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("상품 이미지 경로가 존재하지 않거나, 비어있으면 400 상태를 반환한다.")
        void emptyImageUrl(String imageUrl) throws Exception {
            ProductRequest request = new ProductRequest("치킨", 10000, imageUrl);
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/products/{id}", 1)
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: imageUrl] 상품 이미지는 비어있을 수 없습니다.]"));
        }

        @Test
        @DisplayName("유효한 요청이라면 상품 정보를 수정한다.")
        void updateProduct() throws Exception {
            ProductRequest request = new ProductRequest("치킨", 10000, "http://chicken.com");
            String jsonRequest = objectMapper.writeValueAsString(request);
            willDoNothing().given(productService).updateProduct(anyLong(), any(ProductRequest.class));

            mockMvc.perform(put("/products/{id}", 1)
                            .content(jsonRequest)
                            .contentType(APPLICATION_JSON)
                            .characterEncoding(UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("deleteProduct 메서드는 ")
    class DeleteProduct {

        @ParameterizedTest
        @ValueSource(strings = {"hello", "1.5"})
        @DisplayName("ID가 Long 타입으로 변환할 수 없는 경우 400 상태를 반환한다.")
        void invalidIDType(String id) throws Exception {
            mockMvc.perform(delete("/products/{id}", id))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            String.format("올바르지 않은 타입입니다. 필요한 타입은 [%s]입니다. 현재 입력값: %s", Long.class.getSimpleName(), id)));
        }

        @Test
        @DisplayName("유효한 요청이라면 상품을 삭제한다.")
        void deleteProduct() throws Exception {
            willDoNothing().given(productService).deleteProduct(anyLong());

            mockMvc.perform(delete("/products/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }
}
