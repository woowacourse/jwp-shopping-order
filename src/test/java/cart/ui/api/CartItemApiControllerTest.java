package cart.ui.api;

import cart.application.CartItemService;
import cart.dao.member.JdbcTemplateMemberDao;
import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
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

import javax.sql.DataSource;

import static cart.fixture.MemberFixture.하디;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemApiController.class)
@Import(CartItemApiControllerTest.TestConfig.class)
@Sql("classpath:schema.sql")
public class CartItemApiControllerTest {

    private final static String CREDENTIAL = "basic " + Base64.encodeBase64String((하디.getEmail() + ":" + 하디.getPassword()).getBytes());
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
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartItemService cartItemService;

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
    void 장바구니_아이템_조회성공시_상태코드가_OK_이다() throws Exception {
        // given

        // when, then
        mockMvc.perform(get("/cart-items")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 장바구니_아이템_추가성공시_상태코드가_CREATED_이다() throws Exception {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(1L);

        // when, then
        mockMvc.perform(post("/cart-items")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(cartItemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void 장바구니_아이템_추가시_상품아이디가_양수가아니면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(0L);

        // when, then
        mockMvc.perform(post("/cart-items")
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(cartItemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 장바구니_아이템_수량변경성공시_상태코드가_OK_이다() throws Exception {
        // given
        Long id = 1L;
        CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest = new CartItemQuantityUpdateRequest(3L);

        // when, then
        mockMvc.perform(patch("/cart-items/" + id)
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(cartItemQuantityUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 장바구니_아이템_수량변경시_음수면_상태코드가_BAD_REQUEST_이다() throws Exception {
        // given
        Long id = 1L;
        CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest = new CartItemQuantityUpdateRequest(-3L);

        // when, then
        mockMvc.perform(patch("/cart-items/" + id)
                        .header(AUTH_HEADER, CREDENTIAL)
                        .content(objectMapper.writeValueAsString(cartItemQuantityUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 장바구니_아이템_삭제성공시_상태코드가_NO_CONTENT_이다() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(delete("/cart-items/" + id)
                        .header(AUTH_HEADER, CREDENTIAL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
