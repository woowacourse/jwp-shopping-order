package cart.ui;

import static cart.helper.RestDocsHelper.prettyDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductApiController.class)
@AutoConfigureRestDocs
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @MockBean
    MemberDao memberDao;

    @Test
    void 모든_상품을_조회한다() throws Exception {
        // given
        given(productService.getAllProducts())
                .willReturn(List.of(
                        ProductResponse.of(
                                new Product(1L, "치킨", 20000, "http://image.com/image.png")),
                        ProductResponse.of(
                                new Product(2L, "샐러드", 8000, "http://image.com/image.png")),
                        ProductResponse.of(
                                new Product(3L, "피자", 25000, "http://image.com/image.png"))
                ));

        // expect
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(prettyDocument("products/findAll",
                        relaxedResponseFields(
                                fieldWithPath("result[0].id").description("상품 ID"),
                                fieldWithPath("result[0].name").description("상품의 이름"),
                                fieldWithPath("result[0].price").description("상품의 가격"),
                                fieldWithPath("result[0].imageUrl").description("상품의 이미지 URL")
                        )
                ));
    }

    @Test
    void 상품을_상세_조회한다() throws Exception {
        // given
        given(productService.getProductById(anyLong()))
                .willReturn(ProductResponse.of(
                        new Product(1L, "치킨", 20000, "http://image.com/image.png")
                ));

        // expect
        mockMvc.perform(get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(prettyDocument("products/findById",
                        pathParameters(
                                parameterWithName("id").description("상품 ID")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("result.id").description("상품 ID"),
                                fieldWithPath("result.name").description("상품의 이름"),
                                fieldWithPath("result.price").description("상품의 가격"),
                                fieldWithPath("result.imageUrl").description("상품의 이미지 URL")
                        )
                ));
    }

    @Test
    void 상품을_생성한다() throws Exception {
        // given
        given(productService.createProduct(any(ProductRequest.class)))
                .willReturn(1L);
        ProductRequest request = new ProductRequest("치킨", 10_000L, "http://image.com/image.png");

        // expect
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(prettyDocument("products/create",
                        requestFields(
                                fieldWithPath("name").description("상품의 이름"),
                                fieldWithPath("price").description("상품의 가격"),
                                fieldWithPath("imageUrl").description("상품의 이미지 URL")
                        )
                ));
    }

    @Test
    void 상품을_생성할때_상품의_이름이_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest(null, 10_000L, "http://image.com/image.png");

        // expect
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.name").value("상품 이름은 반드시 포함되어야 합니다."));
    }

    @Test
    void 상품을_생성할때_상품의_가격이_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("사과", null, "http://image.com/image.png");

        // expect
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.price").value("가격은 반드시 포함되어야 합니다."));
    }

    @Test
    void 상품을_생성할때_상품의_가격이_음수이면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("사과", -1L, "http://image.com/image.png");

        // expect
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.price").value("가격은 음수가 될 수 없습니다."));
    }

    @Test
    void 상품을_생성할때_상품의_이미지가_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("사과", 10_000L, null);

        // expect
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.imageUrl").value("상품 이미지는 반드시 포함되어야 합니다."));
    }

    @Test
    void 상품을_수정한다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("치킨", 10_000L, "http://image.com/image.png");

        // expect
        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(prettyDocument("products/update",
                        pathParameters(
                                parameterWithName("id").description("상품 ID")
                        )
                ));
    }

    @Test
    void 상품을_수정할때_상품의_이름이_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest(null, 10_000L, "http://image.com/image.png");

        // expect
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.name").value("상품 이름은 반드시 포함되어야 합니다."));
    }

    @Test
    void 상품을_수정할때_상품의_가격이_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("사과", null, "http://image.com/image.png");

        // expect
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.price").value("가격은 반드시 포함되어야 합니다."));
    }

    @Test
    void 상품을_수정할때_상품의_가격이_음수이면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("사과", -1L, "http://image.com/image.png");

        // expect
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.price").value("가격은 음수가 될 수 없습니다."));
    }

    @Test
    void 상품을_수정할때_상품의_이미지가_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        // given
        ProductRequest request = new ProductRequest("사과", 10_000L, null);

        // expect
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.imageUrl").value("상품 이미지는 반드시 포함되어야 합니다."));
    }

    @Test
    void 상품을_삭제한다() throws Exception {
        // expect
        mockMvc.perform(delete("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(prettyDocument("products/delete",
                        pathParameters(
                                parameterWithName("id").description("상품 ID")
                        )
                ));
    }
}
