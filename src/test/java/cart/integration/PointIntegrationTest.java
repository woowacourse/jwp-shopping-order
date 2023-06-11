package cart.integration;

import static java.util.Base64.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cart.dao.MemberDao;
import cart.dao.PointAdditionDao;
import cart.domain.Member;
import cart.domain.PointAddition;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PointIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private PointAdditionDao pointAdditionDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.getMemberById(1L);
    }

    @Test
    void 적립된_포인트를_조회한다() throws Exception {
        // given
        int point = 200;

        pointAdditionDao.insert(
            new PointAddition(1L, member.getId(), 1L, point, LocalDateTime.now(), LocalDateTime.now().plusDays(90)));

        // when & then
        mockMvc.perform(get("/points")
                .header("Authorization", getEncodedAuthHeader(member)))
            .andExpect(jsonPath("$.currentPoint").value(point))
            .andExpect(jsonPath("$.toBeExpiredPoint").value(0));
    }

    @Test
    void 잘못된_인증_정보로_포인트를_조회하면_Unauthorized가_반환된다() throws Exception {
        // given
        Member invalidMember = new Member(1L, "a@a.com", "7890");

        // when & then
        mockMvc.perform(get("/points")
                .header("Authorization", getEncodedAuthHeader(invalidMember)))
            .andExpect(status().isUnauthorized());
    }

    private String getEncodedAuthHeader(Member member) {
        return "Basic " + new String(
            getEncoder().encode(String.format("%s:%s", member.getEmail(), member.getPassword()).getBytes()));
    }
}
