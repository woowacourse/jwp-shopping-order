package cart.ui;

import cart.Fixture;
import cart.WebMvcConfig;
import cart.application.ProductService;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static cart.ui.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ProductApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class)
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class ProductApiControllerTest {

    @MockBean
    ProductService productService;

    @Autowired
    ProductApiController productApiController;
    @Autowired
    RestDocumentationResultHandler restDocs;
    @Autowired
    ObjectMapper objectMapper;
    MockMvc mockMvc;


    @BeforeEach
    void setUp(@Autowired final RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.standaloneSetup(productApiController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void getAllProducts() throws Exception {
        final ProductResponse response = ProductResponse.of(Fixture.productA);
        given(productService.getAllProducts()).willReturn(List.of(response));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("[0].id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("[0].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("[0].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("[0].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소")
                                )
                        )
                );
    }

    @Test
    void getProductById() throws Exception {
        final ProductResponse response = ProductResponse.of(Fixture.productA);
        given(productService.getProductById(anyLong())).willReturn(response);

        mockMvc.perform(get("/products/{id}", Fixture.productA.getId()))
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
        final Product p = Fixture.productA;
        final ProductRequest request = new ProductRequest(p.getName(), p.getPrice(), p.getImageUrl());
        given(productService.createProduct(any(ProductRequest.class))).willReturn(1L);

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
        final Product p = Fixture.productA;
        final ProductRequest request = new ProductRequest(p.getName(), p.getPrice(), p.getImageUrl());
        willDoNothing().given(productService).updateProduct(anyLong(), any(ProductRequest.class));

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
        willDoNothing().given(productService).deleteProduct(anyLong());

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
