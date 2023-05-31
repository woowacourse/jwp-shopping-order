package cart.application.repository;

import cart.application.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    void update(Member updatedMember);
}
