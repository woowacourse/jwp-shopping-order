package cart.ui;

import static cart.fixtures.ProductFixtures.자몽_허니_블랙티_5700원_ID_2;
import static cart.utils.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.ProductService;
import cart.dto.product.ProductResponse;
import cart.repository.dao.MemberDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductApiController.class)
@AutoConfigureRestDocs
class ProductApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;
    @MockBean
    MemberDao memberDao;

    @Test
    public void createProduct() throws Exception {
        willReturn(1L).given(productService).createProduct(any());

        this.mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"유자 민트 티\","
                                + "\"price\": 1000,"
                                + "\"imageUrl\": \"www.image.png\"}"))
                .andExpect(status().isCreated())
                .andDo(customDocument("create-product",
                        requestFields(
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 이미지")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성된 상품 resource 위치")
                        )
                ));
    }

    @Test
    public void getProductById() throws Exception {
        willReturn(ProductResponse.from(자몽_허니_블랙티_5700원_ID_2)).given(productService).getProductById(2L);

        this.mockMvc.perform(get("/products/{id}", 2L))
                .andExpect(status().isOk())
                .andDo(customDocument("get-product-by-id",
                        pathParameters(
                                parameterWithName("id").description("상품 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 id"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 이미지")
                        )
                ));
    }

    @Test
    public void updateProduct() throws Exception {
        willDoNothing().given(productService).updateProduct(any(), any());

        this.mockMvc.perform(put("/products/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"돌체 라떼\","
                                + "\"price\": 5900,"
                                + "\"imageUrl\": \"https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[128692]_20210426091933665.jpg\"}"))
                .andExpect(status().isOk())
                .andDo(customDocument("update-product",
                        pathParameters(
                                parameterWithName("id").description("상품 id")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 이미지")
                        )
                ));
    }

    @Test
    public void deleteProduct() throws Exception {
        willDoNothing().given(productService).deleteProduct(any());

        this.mockMvc.perform(delete("/products/{id}", 2L))
                .andExpect(status().isNoContent())
                .andDo(customDocument("delete-product",
                        pathParameters(
                                parameterWithName("id").description("상품 id")
                        )
                ));
    }
}
