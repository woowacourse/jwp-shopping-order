package cart.ui.api;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.ui.api.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.configuration.resolver.MemberArgumentResolver;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.SortedOrdersResponse;
import cart.exception.ControllerExceptionHandler;
import java.time.LocalDateTime;
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

class OrderApiControllerTest extends DocsTest {

    @BeforeEach
    void setUp() {
        MemberArgumentResolver memberArgumentResolver = new MemberArgumentResolver(memberDao);

        mockMvc = MockMvcBuilders.standaloneSetup(orderApiController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setCustomArgumentResolvers(memberArgumentResolver)
                .addFilters(new CharacterEncodingFilter(CharEncoding.UTF_8, true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void order() throws Exception {
        OrderRequest request = new OrderRequest(1_000, List.of(1L, 2L));
        given(memberDao.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        given(orderService.order(any(Member.class), anyList(), anyInt())).willReturn(1L);

        mockMvc.perform(post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YUBhLmNvbToxMjM0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                requestFields(
                                        fieldWithPath("usedPoints").type(JsonFieldType.NUMBER).description("소모할 포인트").attributes(field("constraint", "양수")),
                                        fieldWithPath("cartItemIds").type(JsonFieldType.ARRAY).description("주문할 장바구니 상품 ID").attributes(field("constraint", "하나 이상의 주문할 장바구나 ID"))
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("생성된 주문 리소스 ID")
                                )
                        )
                );
    }

    @Test
    void findByOrderId() throws Exception {
        OrderItem orderItem = new OrderItem(CHICKEN, 3);
        Money earnedPoints = new Money(3_000);
        Money usedPoints = new Money(3_000);
        Order order = Order.of(1L, List.of(orderItem), MEMBER_A.getId(), usedPoints, earnedPoints, LocalDateTime.now());
        given(memberDao.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        given(orderService.findByMemberAndOrderId(any(Member.class), anyLong())).willReturn(OrderResponse.from(order));

        mockMvc.perform(get("/orders/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Basic YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                pathParameters(
                                        parameterWithName("id").description("주문 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 ID"),
                                        fieldWithPath("orderItems.[*].productId").type(JsonFieldType.NUMBER).description("주문한 상품 ID"),
                                        fieldWithPath("orderItems.[*].name").type(JsonFieldType.STRING).description("주문한 상품 이름"),
                                        fieldWithPath("orderItems.[*].imageUrl").type(JsonFieldType.STRING).description("주문한 상품 이미지 URL"),
                                        fieldWithPath("orderItems.[*].price").type(JsonFieldType.NUMBER).description("주문한 상품 단일 금액"),
                                        fieldWithPath("orderItems.[*].quantity").type(JsonFieldType.NUMBER).description("주문한 장바구니 상품 수량"),
                                        fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("주문 총 금액"),
                                        fieldWithPath("payPrice").type(JsonFieldType.NUMBER).description("실 사용 금액"),
                                        fieldWithPath("earnedPoints").type(JsonFieldType.NUMBER).description("적립 예정 포인트"),
                                        fieldWithPath("usedPoints").type(JsonFieldType.NUMBER).description("소모한 포인트"),
                                        fieldWithPath("orderDate").type(JsonFieldType.STRING).description("주문 일자")
                                )
                        )
                );
    }

    @Test
    void findByLastIdAndSize() throws Exception {
        OrderItem orderItem = new OrderItem(CHICKEN, 3);
        Money earnedPoints = new Money(3_000);
        Money usedPoints = new Money(3_000);
        Order firstOrder = Order.of(1L, List.of(orderItem), MEMBER_A.getId(), usedPoints, earnedPoints, LocalDateTime.now());
        Order secondOrder = Order.of(2L, List.of(orderItem), MEMBER_A.getId(), usedPoints, earnedPoints, LocalDateTime.now());
        Order thirdOrder = Order.of(3L, List.of(orderItem), MEMBER_A.getId(), usedPoints, earnedPoints, LocalDateTime.now());
        SortedOrdersResponse response = SortedOrdersResponse.from(List.of(thirdOrder, secondOrder, firstOrder));

        given(memberDao.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        given(orderService.findByMemberAndLastOrderId(any(Member.class), anyLong(), anyInt())).willReturn(response);

        mockMvc.perform(get("/orders")
                        .queryParam("last-id", "15")
                        .header(HttpHeaders.AUTHORIZATION, "Basic YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                requestParameters(
                                        parameterWithName("last-id").description("마지막으로 조회한 주문 ID").attributes(field("optional", "true")).attributes(field("constraint", "첫 번째 페이지 요청 시에만 생략"))
                                ),
                                responseFields(
                                        fieldWithPath("orders.[*].id").type(JsonFieldType.NUMBER).description("주문 ID"),
                                        fieldWithPath("orders.[*].orderItems.[*].productId").type(JsonFieldType.NUMBER).description("주문한 상품 ID"),
                                        fieldWithPath("orders.[*].orderItems.[*].name").type(JsonFieldType.STRING).description("주문한 상품 이름"),
                                        fieldWithPath("orders.[*].orderItems.[*].imageUrl").type(JsonFieldType.STRING).description("주문한 상품 이미지 URL"),
                                        fieldWithPath("orders.[*].orderItems.[*].price").type(JsonFieldType.NUMBER).description("주문한 상품 단일 금액"),
                                        fieldWithPath("orders.[*].orderItems.[*].quantity").type(JsonFieldType.NUMBER).description("주문한 장바구니 상품 수량"),
                                        fieldWithPath("orders.[*].totalPrice").type(JsonFieldType.NUMBER).description("주문 총 금액"),
                                        fieldWithPath("orders.[*].payPrice").type(JsonFieldType.NUMBER).description("실 사용 금액"),
                                        fieldWithPath("orders.[*].earnedPoints").type(JsonFieldType.NUMBER).description("적립 예정 포인트"),
                                        fieldWithPath("orders.[*].usedPoints").type(JsonFieldType.NUMBER).description("소모한 포인트"),
                                        fieldWithPath("orders.[*].orderDate").type(JsonFieldType.STRING).description("주문 일자"),
                                        fieldWithPath("lastOrderId").type(JsonFieldType.NUMBER).description("마지막으로 조회한 주문 ID")
                                )
                        )
                );
    }
}
