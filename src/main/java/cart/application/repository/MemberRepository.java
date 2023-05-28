package cart.application.repository;

import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {

    Member findByEmail(String email);

    List<Member> findAll();
}
