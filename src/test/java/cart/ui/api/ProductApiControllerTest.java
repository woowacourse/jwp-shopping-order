package cart.ui.api;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.ui.api.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ControllerExceptionHandler;
import java.util.List;
import org.apache.commons.codec.CharEncoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

class ProductApiControllerTest extends DocsTest {

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productApiController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .addFilters(new CharacterEncodingFilter(CharEncoding.UTF_8, true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void getAllProducts() throws Exception {
        ProductResponse response = ProductResponse.of(CHICKEN);
        given(productService.findAll()).willReturn(List.of(response));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("[*].id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("[*].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("[*].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("[*].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소")
                                )
                        )
                );
    }

    @Test
    void getProductById() throws Exception {
        ProductResponse response = ProductResponse.of(CHICKEN);
        given(productService.findById(anyLong())).willReturn(response);

        mockMvc.perform(get("/products/{id}", CHICKEN.getId()))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("id").description("상품 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소")
                                )
                        )
                );
    }

    @Test
    void createProduct() throws Exception {
        ProductRequest request = new ProductRequest(CHICKEN.getName(), CHICKEN.getPrice(), CHICKEN.getImageUrl());
        given(productService.save(any(ProductRequest.class))).willReturn(1L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름").attributes(field("constraint", "길이가 1 이상인 문자열")),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격").attributes(field("constraint", "양수")),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소").attributes(field("constraint", "http가 포함된 이미지 URL"))
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("생성된 상품 리소스 URL")
                                )
                        )
                );
    }

    @Test
    void updateProduct() throws Exception {
        ProductRequest request = new ProductRequest("A", 1000, "http://image.com");
        willDoNothing().given(productService).update(anyLong(), any(ProductRequest.class));

        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("id").description("상품 ID")
                                ),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름").attributes(field("constraint", "길이가 1 이상인 문자열")),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격").attributes(field("constraint", "양수")),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소").attributes(field("constraint", "http가 포함된 이미지 URL"))
                                )
                        )
                );
    }

    @Test
    void deleteProduct() throws Exception {
        willDoNothing().given(productService).deleteById(anyLong());

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("id").description("상품 ID")
                                )
                        )
                );
    }
}
