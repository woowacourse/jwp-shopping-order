package cart.ui;

import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderApiController.class)
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberDao memberDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("주문 요청을 보낸다.")
    @Test
    void order() throws Exception {
        //given
        given(orderService.order(any(), any()))
                .willReturn(new OrderResponseDto(1L));
        given(memberDao.getMemberByEmail(any()))
                .willReturn(new Member(1L, "email", "password"));

        final OrderRequestDto orderRequestDto = new OrderRequestDto(
                List.of(1L, 2L),
                10000,
                1L
        );

        //when, then
        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk());
    }

    private String basicHeader() {
        final String email = "email";
        final String password = "password";
        return "Basic " + Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }
}
