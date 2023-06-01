package cart.integration;

import static cart.integration.steps.MemberStep.멤버_포인트_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberIntegrationTest extends IntegrationTest {
    private final MemberEntity 멤버_엔티티 = new MemberEntity
            (null, "vero@email", "asdf1234", 20000, null, null);
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDao memberDao;

    @Test
    void 멤버의_포인트를_조회한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);

        // when
        var 응답 = 멤버_포인트_조회_요청(멤버);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(응답.jsonPath().getInt("point")).isEqualTo(20000)
        );
    }

    private Member 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 저장된_멤버_ID = memberDao.save(멤버_엔티티);
        return memberRepository.findById(저장된_멤버_ID);
    }
}
