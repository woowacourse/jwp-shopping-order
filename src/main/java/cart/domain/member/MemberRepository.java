package cart.domain.member;

import cart.domain.member.dto.MemberWithId;
import java.util.List;

public interface MemberRepository {

    long insert(final Member member);

    Member findById(final Long id);

    Member findByName(final String name);

    List<MemberWithId> findAll();

    boolean existByName(final String memberName);

    Member findMyCouponsByName(final String memberName);
}
