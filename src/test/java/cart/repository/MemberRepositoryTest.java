package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.Member;
import cart.domain.Point;
import cart.exception.MemberNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest extends RepositoryTest {

    private long insertMemberEntityAndGetId() {
        return memberDao.insert(dummyMemberEntity);
    }

    @Test
    void 회원_객체를_저장한다() {
        // when
        long savedId = insertMemberEntityAndGetId();

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @Test
    void ID로_회원_객체를_조회한다() {
        // given
        long savedId = insertMemberEntityAndGetId();

        // when
        Member result = memberRepository.findById(savedId);

        // then
        assertThat(result).isEqualTo(dummyMember);
    }

    @Test
    void 이메일로_회원_객체를_조회한다() {
        // given
        insertMemberEntityAndGetId();

        // when
        Member result = memberRepository.findByEmail(dummyMemberEntity.getEmail());

        // then
        assertThat(result).isEqualTo(dummyMember);
    }

    @Test
    void ID로_존재하지_않는_회원을_조회하면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> memberRepository.findById(2143214L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 이메일로_존재하지_않는_회원을_조회하면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> memberRepository.findByEmail("xxx@email.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 모든_회원을_조회한다() {
        // given
        long savedId = insertMemberEntityAndGetId();

        // when
        List<Member> result = memberRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 회원을_수정한다() {
        // given
        long savedId = insertMemberEntityAndGetId();
        Member newMember = new Member(savedId, "newEmail", "newPassword", new Point(10));

        // when
        memberRepository.update(newMember);

        // then
        Member result = memberRepository.findById(savedId);
        assertThat(result.getEmail()).isEqualTo("newEmail");
        assertThat(result.getPassword()).isEqualTo("newPassword");
        assertThat(result.getPointValue()).isEqualTo(10);
    }

    @Test
    void 회원을_삭제한다() {
        // given
        long savedId = insertMemberEntityAndGetId();
        Member savedMember = memberRepository.findById(savedId);

        // when
        memberRepository.delete(savedMember);

        // then
        assertThatThrownBy(() -> memberRepository.findById(savedId))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
