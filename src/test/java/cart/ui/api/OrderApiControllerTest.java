package cart.ui.api;

import cart.application.AuthService;
import cart.application.OrderService;
import cart.dao.member.JdbcTemplateMemberDao;
import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

import static cart.fixture.MemberFixture.하디;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderApiController.class)
@Import(OrderApiControllerTest.TestConfig.class)
@Sql("classpath:schema.sql")
public class OrderApiControllerTest {

    private final static String CREDENTIAL = "Basic " + Base64.encodeBase64String((하디.getEmail() + ":" + 하디.getPassword()).getBytes());
    private final static String AUTH_HEADER = "Authorization";
    private static Member 하디_멤버;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public DataSource dataSource() {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:mem:test;MODE=MySQL");
            config.setUsername("sa");
            config.setPassword("");
            return new HikariDataSource(config);
        }

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource());
        }

        @Bean
        public MemberDao memberDao() {
            return new JdbcTemplateMemberDao(jdbcTemplate());
        }

        @Bean
        public AuthService authService() {
            return new AuthService(memberDao());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        memberDao.addMember(하디);
    }

    @AfterEach
    void clear() {
        하디_멤버 = memberDao.findMemberByEmail(하디.getEmail()).get();
        memberDao.deleteMember(하디_멤버.getId());
    }

    @Test
    void 주문_생성시_성공하면_상태코드가_CREATED_이다() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 0L, 30L);
        when(orderService.orderCartItems(하디_멤버, orderRequest))
                .thenReturn(10L);

        // when, then
        mockMvc.perform(post("/orders")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void 주문_생성시_사용_포인트가_음수면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), -1L, 30L);

        // when, then
        mockMvc.perform(post("/orders")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_생성시_상품총가격이_음수면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 3L, -1L);

        // when, then
        mockMvc.perform(post("/orders")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_생성시_장바구니_아이디가_음수면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(-1L, 2L), 3L, 1L);

        // when, then
        mockMvc.perform(post("/orders")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_생성시_장바구니_아이디의_사이즈가_0이하이면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(Collections.emptyList(), 3L, 1L);

        // when, then
        mockMvc.perform(post("/orders")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 특정_주문_조회시_성공하면_상태코드가_OK_이다() throws Exception {
        // given
        given(orderService.getOrderById(하디_멤버, 10L))
                .willReturn(new OrderResponse(10L, "111", Collections.emptyList(), 10L, 10L, 10L));

        // when, then
        mockMvc.perform(get("/orders/" + 10L)
                        .header(AUTH_HEADER, CREDENTIAL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 특정_주문_조회시_id가_양수가_아니면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        Long wrongOrderId = -1L;

        // when, then
        mockMvc.perform(get("/orders/" + wrongOrderId)
                        .header(AUTH_HEADER, CREDENTIAL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 전체_주문_조회시_성공하면_상태코드가_OK_이다() throws Exception {
        // given
        given(orderService.getOrdersByMember(하디_멤버))
                .willReturn(Collections.emptyList());

        // when, then
        mockMvc.perform(get("/orders")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
