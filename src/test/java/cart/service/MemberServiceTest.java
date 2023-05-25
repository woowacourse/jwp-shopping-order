package cart.service;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.MemberDto;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;

    @Test
    void 모든_사용자를_조회한다() {
        // given
        final Member member1 = memberDao.save(new Member("pizza1@pizza.com", "password"));
        final Member member2 = memberDao.save(new Member("pizza2@pizza.com", "password"));

        // when
        final List<MemberDto> result = memberService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new MemberDto(member1.getId(), member1.getEmail(), member1.getPassword()),
                new MemberDto(member2.getId(), member2.getEmail(), member2.getPassword())
        ));
    }
}
