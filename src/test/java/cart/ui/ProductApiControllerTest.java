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
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.ProductService;
import cart.dao.MemberDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductApiController.class)
@AutoConfigureRestDocs
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
    @DisplayName("모든 상품을 조회한다.")
    void getAllProducts() throws Exception {
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
                                fieldWithPath("[0].id").description("상품 ID"),
                                fieldWithPath("[0].name").description("상품의 이름"),
                                fieldWithPath("[0].price").description("상품의 가격"),
                                fieldWithPath("[0].imageUrl").description("상품의 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("상품을 상세 조회한다.")
    void getProductById() throws Exception {
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
                        responseFields(
                                fieldWithPath("id").description("상품 ID"),
                                fieldWithPath("name").description("상품의 이름"),
                                fieldWithPath("price").description("상품의 가격"),
                                fieldWithPath("imageUrl").description("상품의 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() throws Exception {
        // given
        given(productService.createProduct(any(ProductRequest.class)))
                .willReturn(1L);
        ProductRequest request = new ProductRequest("치킨", 10000, "http://image.com/image.png");

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
    @DisplayName("상품을 수정한다.")
    void updateProduct() throws Exception {
        // given
        ProductRequest request = new ProductRequest("치킨", 10000, "http://image.com/image.png");

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
    @DisplayName("상품을 삭제한다.")
    void deleteProduct() throws Exception {
        // expect
        mockMvc.perform(delete("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(prettyDocument("products/delete",
                        pathParameters(
                                parameterWithName("id").description("상품 ID")
                        )
                ));
    }
}
