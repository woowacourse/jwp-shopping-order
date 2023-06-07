package cart.repository;

import cart.domain.member.Member;

import java.util.List;

public interface MemberRepository {

     Member getMemberById(Long id);

     Member getMemberByEmail(String email);

     List<Member> getAllMember();
}
