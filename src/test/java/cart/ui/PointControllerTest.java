package cart.ui;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dao.MemberDao;
import cart.entity.MemberEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberDao memberDao;

    private String basicAuthHeader;

    @BeforeEach
    void setUp() {
        basicAuthHeader = "Basic " + Base64Utils.encodeToString("id:password".getBytes());

        given(memberDao.findByEmail("id"))
                .willReturn(Optional.of(new MemberEntity(1L, "id", "password", 400)));
    }

    @Test
    @DisplayName("멤버의 포인트를 조회한다.")
    void findPointOfMember() throws Exception {
        mockMvc.perform(get("/points")
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(400));
    }

    @Test
    @DisplayName("결제금액을 통해 예상 적립 금액을 조회한다.")
    void calculatePointByPayment() throws Exception {
        mockMvc.perform(get("/saving-point?totalPrice={price}", 40000))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.savingPoint").value(4000));
    }

    @Test
    @DisplayName("잘못된 결제 금액이 들어올 경우 400 응답을 반환한다.")
    void calculatePointByInvalidPayment() throws Exception {
        mockMvc.perform(get("/saving-point?totalPrice={price}", -4000))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
