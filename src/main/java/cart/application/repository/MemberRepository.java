package cart.application.repository;

import cart.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Long createMember(Member member);

    List<Member> findAllMembers();

    Optional<Member> findMemberById(Long id);

    Optional<Member> findMemberByEmail(String email);

    Boolean isMemberExist(String email, String password);

}
