package cart.member.repository;

import cart.init.DBInit;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.member.domain.MemberTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.member.domain.MemberTest.*;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberRepositoryTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberRepository = new MemberRepository(memberDao);
    }

    @Test
    void 이메일로_회원을_조회한다() {
        // given
        final String email = "a@a.com";

        // when
        final Member memberByEmail = memberRepository.getMemberByEmail(email);

        // then
        assertThat(memberByEmail).isEqualTo(MEMBER);
    }

    @Test
    void memberId로_회원을_조회한다() {
        // given
        final long memberId = 1L;

        // when
        final Member memberById = memberRepository.getMemberById(memberId);

        // then
        assertThat(memberById).isEqualTo(MEMBER);
    }
}
