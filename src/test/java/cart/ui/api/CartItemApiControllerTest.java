package cart.ui.api;

import static cart.fixture.DomainFixture.FOUR_SALAD;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.TWO_CHICKEN;
import static cart.fixture.ValueFixture.MEMBER_A_BASIC_TOKEN;
import static cart.ui.api.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.authentication.AuthenticationInterceptor;
import cart.authentication.AuthenticationMemberConverter;
import cart.authentication.BasicAuthorizationExtractor;
import cart.authentication.MemberStore;
import cart.configuration.resolver.MemberArgumentResolver;
import cart.domain.Member;
import cart.domain.Money;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.CheckoutResponse;
import cart.exception.ControllerExceptionHandler;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

class CartItemApiControllerTest extends DocsTest {

    @BeforeEach
    void setUp() {
        MemberStore memberStore = new MemberStore();
        AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor(
                new BasicAuthorizationExtractor(),
                new AuthenticationMemberConverter(memberRepository),
                memberStore
        );
        MemberArgumentResolver memberArgumentResolver = new MemberArgumentResolver(memberStore);

        mockMvc = MockMvcBuilders.standaloneSetup(cartItemApiController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setCustomArgumentResolvers(memberArgumentResolver)
                .addInterceptors(authInterceptor)
                .addFilters(new CharacterEncodingFilter(CharEncoding.UTF_8, true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void showCartItems() throws Exception {
        CartItemResponse response = CartItemResponse.of(TWO_CHICKEN);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        given(cartItemService.findByMember(any(Member.class))).willReturn(List.of(response));

        mockMvc.perform(get("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, MEMBER_A_BASIC_TOKEN))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                responseFields(
                                        fieldWithPath("[*].id").type(JsonFieldType.NUMBER).description("장바구니 상품 ID"),
                                        fieldWithPath("[*].quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량"),
                                        fieldWithPath("[*].product.id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("[*].product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("[*].product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("[*].product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소")
                                )
                        )
                );
    }

    @Test
    void addCartItems() throws Exception {
        CartItemRequest request = new CartItemRequest(1L);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        given(cartItemService.add(any(Member.class), any(CartItemRequest.class))).willReturn(1L);

        mockMvc.perform(post("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, MEMBER_A_BASIC_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                requestFields(
                                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID").attributes(field("constraint", "양수"))
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("생성된 장바구니 상품 리소스 URL")
                                )
                        )
                );

    }

    @Test
    void updateCartItemQuantity() throws Exception {
        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(3);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        willDoNothing().given(cartItemService).updateQuantity(any(Member.class), anyLong(), any(CartItemQuantityUpdateRequest.class));

        mockMvc.perform(patch("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, MEMBER_A_BASIC_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                pathParameters(
                                        parameterWithName("id").description("장바구니 상품 ID")
                                ),
                                requestFields(
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량").attributes(field("constraint", "양수"))
                                )
                        )
                );
    }

    @Test
    void removeCartItems() throws Exception {
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        willDoNothing().given(cartItemService).remove(any(Member.class), anyLong());

        mockMvc.perform(delete("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, MEMBER_A_BASIC_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                pathParameters(
                                        parameterWithName("id").description("장바구니 상품 ID")
                                )
                        )
                );
    }
    
    @Test
    void checkout() throws Exception {
        CheckoutResponse checkoutResponse = CheckoutResponse.of(
                List.of(TWO_CHICKEN, FOUR_SALAD),
                MEMBER_A,
                new Money(100_000),
                new Money(10_000),
                new Money(3_500)
        );
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        given(cartItemService.checkout(any(Member.class), anyList())).willReturn(checkoutResponse);

        mockMvc.perform(
                get("/cart-items/checkout")
                        .header(HttpHeaders.AUTHORIZATION, MEMBER_A_BASIC_TOKEN)
                        .queryParam("ids", "1,2,3")
                )
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                requestParameters(
                                        parameterWithName("ids").description("장바구니에서 주문할 상품 ID").attributes(field("optional", "false")).attributes(field("constraint", "구분자 ','를 사용한 숫자"))
                                ),
                                responseFields(
                                        fieldWithPath("cartItems.[*].id").type(JsonFieldType.NUMBER).description("장바구니 상품 ID"),
                                        fieldWithPath("cartItems.[*].quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량"),
                                        fieldWithPath("cartItems.[*].product.id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("cartItems.[*].product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("cartItems.[*].product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 URL"),
                                        fieldWithPath("cartItems.[*].product.price").type(JsonFieldType.NUMBER).description("상품 단일 금액"),
                                        fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("주문 총 금액"),
                                        fieldWithPath("currentPoints").type(JsonFieldType.NUMBER).description("현재 소유 포인트"),
                                        fieldWithPath("earnedPoints").type(JsonFieldType.NUMBER).description("적립 예정 포인트"),
                                        fieldWithPath("availablePoints").type(JsonFieldType.NUMBER).description("적립 에정 포인트")
                                )
                        )
                );
    }
}
