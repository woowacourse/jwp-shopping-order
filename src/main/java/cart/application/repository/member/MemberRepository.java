package cart.application.repository.member;

import cart.domain.member.Member;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface MemberRepository {

    Long createMember(final Member member);

    List<Member> findAllMembers();

    Optional<Member> findMemberById(final Long id);

    default Member getById(final Long id) {
        return findMemberById(id)
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));
    }

    default Member getByEmail(final String email) {
        return findMemberByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("일치하는 사용자가 없습니다."));
    }

    Optional<Member> findMemberByEmail(final String email);

}
