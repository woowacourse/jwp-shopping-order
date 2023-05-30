package cart.controller.product;

import cart.domain.product.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.sale.SaleProductRequest;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import cart.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.helper.RestDocsHelper.customDocument;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductController.class)
@AutoConfigureRestDocs
class ProductControllerUnitTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CouponRepository couponRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void find_all_products() throws Exception {
        // given
        List<ProductResponse> result = List.of(ProductResponse.from(new Product(1L, "치킨", 10000, "img", false, 0)));
        when(productService.findAllProducts()).thenReturn(result);

        // when & then
        mockMvc.perform(get("/products")
                        .header("Authorization", "member")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("치킨"))
                .andExpect(jsonPath("$[0].price").value(10000))
                .andExpect(jsonPath("$[0].imageUrl").value("img"))
                .andExpect(jsonPath("$[0].isOnSale").value("false"))
                .andExpect(jsonPath("$[0].salePrice").value(0))
                .andDo(customDocument("find_all_products",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("[0].id").description(1L),
                                fieldWithPath("[0].name").description("치킨"),
                                fieldWithPath("[0].price").description(10000),
                                fieldWithPath("[0].imageUrl").description("img"),
                                fieldWithPath("[0].isOnSale").description(false),
                                fieldWithPath("[0].salePrice").description(0)
                        )
                ));
    }

    @DisplayName("단건 상품을 조회한다.")
    @Test
    void find_product_by_id() throws Exception {
        // given
        Long id = 1L;
        ProductResponse result = ProductResponse.from(new Product(1L, "치킨", 10000, "img", false, 0));
        when(productService.findProductById(id)).thenReturn(result);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/products/{id}", id)
                        .header("Authorization", "member")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("치킨"))
                .andExpect(jsonPath("$.price").value(10000))
                .andExpect(jsonPath("$.imageUrl").value("img"))
                .andExpect(jsonPath("$.isOnSale").value("false"))
                .andExpect(jsonPath("$.salePrice").value(0))
                .andDo(customDocument("find_product_by_id",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        pathParameters(
                                parameterWithName("id").description("product_id")
                        ),
                        responseFields(
                                fieldWithPath("id").description(1L),
                                fieldWithPath("name").description("치킨"),
                                fieldWithPath("price").description(10000),
                                fieldWithPath("imageUrl").description("img"),
                                fieldWithPath("isOnSale").description(false),
                                fieldWithPath("salePrice").description(0)
                        )
                ));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create_product() throws Exception {
        // given
        ProductRequest request = new ProductRequest("치킨", 10000, "img");
        when(productService.createProduct(request)).thenReturn(1L);

        // when & then
        mockMvc.perform(post("/products")
                        .header("Authorization", "member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                ).andExpect(status().isCreated())
                .andDo(customDocument("create_product",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("productName").description("치킨"),
                                fieldWithPath("productPrice").description(1000),
                                fieldWithPath("imageUrl").description("img")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("/products/{createdId}")
                        )

                ));
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void update_product() throws Exception {
        // given
        ProductRequest request = new ProductRequest("치킨", 10000, "img");

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/products/{id}", 1L)
                        .header("Authorization", "member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                ).andExpect(status().isOk())
                .andDo(customDocument("update_product",
                        pathParameters(
                                parameterWithName("id").description("product_id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("productName").description("치킨"),
                                fieldWithPath("productPrice").description(1000),
                                fieldWithPath("imageUrl").description("img")
                        )
                ));
    }

    @DisplayName("상품을 제거한다.")
    @Test
    void delete_product() throws Exception {
        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/products/{id}", 1L)
                        .header("Authorization", "member")
                ).andExpect(status().isNoContent())
                .andDo(customDocument("delete_product",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        pathParameters(
                                parameterWithName("id").description("product id")
                        )
                ));
    }

    @DisplayName("세일을 적용한다.")
    @Test
    void apply_sale() throws Exception {
        // given
        Long id = 1L;
        SaleProductRequest request = new SaleProductRequest(10);
        when(productService.applySale(id, request)).thenReturn(1L);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/products/sales/{id}", id)
                        .header("Authorization", "member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        )
                ).andExpect(status().isCreated())
                .andDo(customDocument("apply_sale",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("amount").description("10")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("/products/sales/{id}")
                        )

                ));
    }

    @DisplayName("세일을 제거한다.")
    @Test
    void un_apply_sale() throws Exception {
        // given
        Long id = 1L;
        when(productService.unapplySale(id)).thenReturn(id);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/products/sales/{id}", id)
                        .header("Authorization", "member")
                ).andExpect(status().isOk())
                .andDo(customDocument("unapply_sale",
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("/products/sales/{id}")
                        )

                ));
    }
}
