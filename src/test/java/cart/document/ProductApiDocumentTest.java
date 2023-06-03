package cart.document;

import cart.auth.WebMvcConfig;
import cart.cartitem.application.CartItemService;
import cart.product.application.ProductService;
import cart.product.application.dto.ProductCartItemDto;
import cart.product.domain.Product;
import cart.product.ui.ProductApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import java.util.List;

import static cart.fixtures.CartItemFixtures.MemberA_CartItem1;
import static cart.fixtures.CartItemFixtures.MemberA_CartItem2;
import static cart.fixtures.MemberFixtures.MemberA;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PANCAKE;
import static cart.fixtures.ProductFixtures.PIZZA;
import static cart.fixtures.ProductFixtures.SALAD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(ProductApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductApiDocumentTest {

    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartItemService cartItemService;

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
    void 특정_상품_목록_페이징_문서화() throws Exception {
        // given
        final List<Product> products = List.of(SALAD.ENTITY, CHICKEN.ENTITY);
        final List<ProductCartItemDto> productCartItems = List.of(
                new ProductCartItemDto(SALAD.ENTITY, MemberA_CartItem2.ENTITY),
                new ProductCartItemDto(CHICKEN.ENTITY, MemberA_CartItem1.ENTITY)
        );

        given(productService.getProductsInPaging(3L, 2))
                .willReturn(products);
        given(productService.getProductCartItemsByProduct(any(), any()))
                .willReturn(productCartItems);
        given(productService.hasLastProduct(3L, 2))
                .willReturn(true);
        final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/products/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("lastId", "3")
                        .param("pageItemCount", "2"))
                .andExpect(status().isOk())
                .andDo(document("products/getHomePagingProduct",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("lastId").description("현재 페이지의 마지막 상품 아이디"),
                                parameterWithName("pageItemCount").description("한 페이지에 있는 상품 개수")
                        ),
                        responseFields(
                                fieldWithPath("products").type(JsonFieldType.ARRAY).description("상품 목록"),
                                fieldWithPath("products.[].product.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                fieldWithPath("products.[].product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("products.[].product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("products.[].product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 경로"),
                                fieldWithPath("products.[].cartItem.id").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                                fieldWithPath("products.[].cartItem.quantity").type(JsonFieldType.NUMBER).description("장바구니 수량"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("가져온 상품 목록에 마지막 상품이 들어있는지 여부")
                        )
                ));
    }

    @Test
    void 특정_상품_조회_문서화() throws Exception {
        // given
        given(productService.getProductById(any()))
                .willReturn(CHICKEN.ENTITY);

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

    @Nested
    class 멤버와_상품_id를_통한_상품과_장바구니_조회_문서화 {

        @Test
        void 특정_상품이_장바구니에_존재할_때의_문서화() throws Exception {
            // given
            given(productService.getProductById(any()))
                    .willReturn(CHICKEN.ENTITY);
            given(cartItemService.findByMemberAndProduct(any(), any()))
                    .willReturn(MemberA_CartItem1.ENTITY);
            final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());

            // when, then
            mockMvc.perform(get("/products/{productId}/cart-items", CHICKEN.ID)
                            .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(document("products/getProductCartItemByProductId/existCartItem",
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("[Basic Auth] 로그인 정보")
                            ),
                            pathParameters(
                                    parameterWithName("productId").description("조회할 상품 아이디")
                            ),
                            responseFields(
                                    fieldWithPath("product").type(JsonFieldType.OBJECT).description("상품"),
                                    fieldWithPath("product.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                    fieldWithPath("product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                    fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                    fieldWithPath("product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 경로"),
                                    fieldWithPath("cartItem").type(JsonFieldType.OBJECT).description("장바구니"),
                                    fieldWithPath("cartItem.id").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                                    fieldWithPath("cartItem.quantity").type(JsonFieldType.NUMBER).description("장바구니 상품의 개수")
                            )
                    ));
        }

        @Test
        void 특정_상품이_장바구니에_존재하지_않을_때의_문서화() throws Exception {
            // given
            given(productService.getProductById(any()))
                    .willReturn(PANCAKE.ENTITY);
            given(cartItemService.findByMemberAndProduct(any(), any()))
                    .willReturn(null);
            final String encodeAuthInfo = Base64Utils.encodeToString((MemberA.EMAIL + ":" + MemberA.PASSWORD).getBytes());

            // when, then
            mockMvc.perform(get("/products/{productId}/cart-items", PANCAKE.ID)
                            .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(document("products/getProductCartItemByProductId/notExistCartItem",
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("productId").description("조회할 상품 아이디")
                            ),
                            responseFields(
                                    fieldWithPath("product").type(JsonFieldType.OBJECT).description("상품"),
                                    fieldWithPath("product.id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                                    fieldWithPath("product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                    fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                    fieldWithPath("product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 경로"),
                                    fieldWithPath("cartItem").type(JsonFieldType.NULL).description("장바구니가 없으므로 NULL을 반환")
                            )
                    ));
        }
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
        willDoNothing().given(productService).updateProduct(CHICKEN.ID, CHICKEN.ENTITY);

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
