package cart.controller;

import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrderInfo;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.ui.OrdersApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(OrdersApiController.class)
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberDao memberDao;

    @MockBean
    private OrderService orderService;

    @DisplayName("장바구니에 담긴 상품을 주문한다")
    @Test
    void order() throws Exception {
        String body = objectMapper.writeValueAsString(
                new OrderRequest(new ArrayList<Long>(List.of(1L, 2L)), 23000, 5000, 2300));

        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(new Member(1L, "a@a.com", "password1"));
        when(orderService.orderItems(anyLong(), any())).thenReturn(1L);

        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE=")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/1"));
    }

    @DisplayName("사용자별로 주문 목록을 확인할 수 있다")
    @Test
    void getPastOrders() throws Exception {
        Member member = new Member(1L, "a@a.com", "password1");
        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(member);
        when(orderService.findOrdersByMember(member))
                .thenReturn(List.of(new OrderResponse(1L, List.of(new OrderInfo(1L, 10000, "치킨", "imageUrl", 1)))));

        this.mockMvc.perform(get("/orders")
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].orderId").exists())
                .andExpect(jsonPath("$[*].orderInfo[*].productId").exists())
                .andExpect(jsonPath("$[*].orderInfo[*].price").exists())
                .andExpect(jsonPath("$[*].orderInfo[*].name").exists())
                .andExpect(jsonPath("$[*].orderInfo[*].imageUrl").exists())
                .andExpect(jsonPath("$[*].orderInfo[*].quantity").exists());
    }
}
