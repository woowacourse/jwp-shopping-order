package cart.controller;

import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrdersRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
                new OrdersRequest(new ArrayList<Long>(List.of(1L, 2L)), 23000, 5000, 2300));

        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(new Member(1L, "a@a.com", "password1"));
        when(orderService.orderItems(anyLong(), any())).thenReturn(1L);

        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE=")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/1"));
    }
}
