package cart.application.repository;

import cart.application.domain.Member;
import java.util.List;

public interface MemberRepository {

    List<Member> findAll();
}
