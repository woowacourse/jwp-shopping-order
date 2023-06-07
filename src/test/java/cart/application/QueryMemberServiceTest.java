package cart.application;

import cart.application.response.QueryMemberResponse;
import cart.config.ServiceTestConfig;
import cart.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QueryMemberServiceTest extends ServiceTestConfig {

    QueryMemberService queryMemberService;

    @BeforeEach
    void setUp() {
        queryMemberService = new QueryMemberService(memberRepository);
    }

    @Test
    void 회원_식별자값으로_회원을_조회한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");

        // when
        QueryMemberResponse 회원_응답 = queryMemberService.findByMemberId(회원.getId());

        // then
        assertThat(회원_응답)
                .usingRecursiveComparison()
                .isEqualTo(new QueryMemberResponse(회원.getId(), "a@a.com", 10000, 1000));
    }
}
