package cart.controller.order;

import cart.config.auth.guard.basic.MemberArgumentResolver;
import cart.config.web.WebMvcConfig;
import cart.domain.history.CouponHistory;
import cart.domain.history.OrderHistory;
import cart.domain.history.ProductHistory;
import cart.domain.member.Member;
import cart.dto.order.OrdersResponse;
import cart.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.fixture.CouponFixture.createCoupons;
import static cart.fixture.MemberFixture.createMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureRestDocs
public class OrderControllerUnitTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberArgumentResolver memberArgumentResolver;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("모든 주문 내역을 조회한다.")
    @Test
    void find_all_orders() throws Exception {
        // given
        Member member = createMember();
        member.initCoupons(createCoupons());
        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);


        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        OrderHistory orderHistory = new OrderHistory(
                1L,
                List.of(new ProductHistory(1L, "치킨", "img", 10, 10000)),
                3000,
                List.of(new CouponHistory(1, "coupon")),
                "2022-05-29"
        );

        OrdersResponse ordersResponse = OrdersResponse.from(List.of(orderHistory));
        when(orderService.findOrders(any(Member.class))).thenReturn(ordersResponse);

        // when & then
        mockMvc.perform(get("/orders")
                        .header("Authorization", "Basic YUBhLmNvbToxMjM0")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[0].orderTime").value("2022-05-29"))
                .andExpect(jsonPath("$[0].products[0].productId").value(1))
                .andExpect(jsonPath("$[0].products[0].productName").value("치킨"))
                .andExpect(jsonPath("$[0].products[0].imgUrl").value("img"))
                .andExpect(jsonPath("$[0].products[0].quantity").value(10))
                .andExpect(jsonPath("$[0].products[0].price").value(10000))
                .andExpect(jsonPath("$[0].deliveryPrice.deliveryPrice").value("3000"))
                .andExpect(jsonPath("$[0].coupons[0].couponId").value("1"))
                .andExpect(jsonPath("$[0].coupons[0].couponName").value("coupon"));
//                .andDo(customDocument("find_all_products",
//                        requestHeaders(
//                                headerWithName("Authorization").description("Basic auth credentials")
//                        ),
//                        responseFields(
//                                fieldWithPath("[0].orderId").description(1),
//                                fieldWithPath("[0].name").description("치킨"),
//                                fieldWithPath("[0].price").description(10000),
//                                fieldWithPath("[0].imageUrl").description("img"),
//                                fieldWithPath("[0].isOnSale").description(false),
//                                fieldWithPath("[0].salePrice").description(0)
//                        )
//                ));
    }
}
