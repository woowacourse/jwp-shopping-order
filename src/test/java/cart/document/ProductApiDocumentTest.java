package cart.document;

import cart.WebMvcConfig;
import cart.application.ProductService;
import cart.ui.ProductApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PIZZA;
import static cart.fixtures.ProductFixtures.SALAD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(ProductApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductApiDocumentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @Test
    void 모든_상품_목록_조회_문서화() throws Exception {
        // given
        given(productService.getAllProducts())
                .willReturn(List.of(CHICKEN.ENTITY, SALAD.ENTITY, PIZZA.ENTITY));

        // when, then
        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("products/getAllProducts",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"))
                        )
                );
    }

    @Test
    void 특정_상품_조회_문서화() throws Exception {
        // given
        given(productService.getProductById(any()))
                .willReturn(CHICKEN.DOMAIN);

        // when, then
        mockMvc.perform(get("/products/{id}", CHICKEN.ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("products/getProductById",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("조회할 상품 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL")
                        )
                ));
    }

    @Test
    void 상품_추가_문서화() throws Exception {
        // given
        given(productService.createProduct(any()))
                .willReturn(CHICKEN.ID);

        // when, then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(CHICKEN.REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("products/createProduct",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL")
                                )
                        )
                );
    }

    @Test
    void 상품_수정_문서화() throws Exception {
        // given
        willDoNothing().given(productService).updateProduct(CHICKEN.ID, CHICKEN.REQUEST);

        // when, then
        mockMvc.perform(put("/products/{id}", CHICKEN.ID)
                        .content(objectMapper.writeValueAsString(CHICKEN.REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("products/updateProduct",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("수정할 상품 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("수정할 상품 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("수정할 상품 가격"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("수정할 상품 이미지 URL")
                        )
                ));
    }

    @Test
    void 상품_삭제_문서화() throws Exception {
        // given
        willDoNothing().given(productService).deleteProduct(CHICKEN.ID);

        // when, then
        mockMvc.perform(delete("/products/{id}", CHICKEN.ID))
                .andExpect(status().isNoContent())
                .andDo(document("products/deleteProduct",
                                pathParameters(
                                        parameterWithName("id").description("삭제할 상품 아이디")
                                )
                        )
                );
    }
}
