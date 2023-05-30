package cart.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

	void addMember(Member member);

	List<Member> findAll();

	Optional<Member> findById(Long id);

	Optional<Member> findByEmail(String email);

	void update(Member member);

	void deleteById(Long id);
}
