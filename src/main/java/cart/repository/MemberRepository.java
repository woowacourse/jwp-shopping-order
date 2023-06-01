package cart.repository;

import cart.domain.Member;
import java.util.List;

public interface MemberRepository {

    List<Member> findAll();
}
