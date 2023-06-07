package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.ErrorMessage;
import cart.exception.MemberException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberRepositoryTest {
    private final MemberEntity 멤버_엔티티 = new MemberEntity(
            null, "vero@email", "password", 20000, null, null
    );

    private final MemberEntity 두번째_멤버_엔티티 = new MemberEntity(
            null, "other@email", "other_password", 2000, null, null
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberRepository memberRepository;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        memberRepository = new MemberRepository(memberDao);
    }

    @Test
    void 아이디로_멤버를_조회한다() {
        // given
        Member ID가_있는_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        // when
        Member 저장된_멤버 = memberRepository.findById(ID가_있는_멤버.getId());

        // then
        assertThat(저장된_멤버).isEqualTo(ID가_있는_멤버);
    }

    private Member 멤버를_저장하고_ID가_있는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 멤버_ID = memberDao.save(멤버_엔티티);

        return new Member(멤버_ID, 멤버_엔티티.getEmail(), 멤버_엔티티.getPassword(), 멤버_엔티티.getPoint());
    }

    @Test
    void 존재하지_않는_멤버를_조회하는_경우_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> memberRepository.findById(Long.MAX_VALUE))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    void 이메일로_멤버를_조회한다() {
        // given
        Member ID가_있는_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        // when
        Member 저장된_멤버 = memberRepository.findByEmail(ID가_있는_멤버.getEmail());

        // then
        assertThat(저장된_멤버).isEqualTo(ID가_있는_멤버);
    }

    @Test
    void 모든_멤버를_조회한다() {
        // given
        Member ID가_있는_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);
        Member ID가_있는_두번째_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(두번째_멤버_엔티티);

        // when
        List<Member> 저장된_멤버들 = memberRepository.findAll();

        // then
        assertAll(
                () -> assertThat(저장된_멤버들).hasSize(2),
                () -> assertThat(저장된_멤버들).usingRecursiveComparison()
                        .isEqualTo(List.of(ID가_있는_멤버, ID가_있는_두번째_멤버))
        );
    }

    @Test
    void 멤버의_포인트를_수정한다() {
        // given
        Member ID가_있는_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);
        int 이전_포인트 = ID가_있는_멤버.getPoint();
        ID가_있는_멤버.addPoint(20000);

        // when
        memberRepository.updatePoint(ID가_있는_멤버);
        Member 변경된_멤버 = memberRepository.findById(ID가_있는_멤버.getId());

        // then
        assertThat(이전_포인트 + 20000 / 10).isEqualTo(변경된_멤버.getPoint());
    }

    @Test
    void 멤버로_멤버의_포인트를_찾는다() {
        // given
        Member ID가_있는_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        // when
        int 저장된_포인트 = memberRepository.findPointByMember(ID가_있는_멤버);

        // then
        assertThat(저장된_포인트).isEqualTo(멤버_엔티티.getPoint());
    }
}
