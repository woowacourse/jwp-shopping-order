package cart.domain.member;

import java.util.List;

public interface MemberRepository {

    long insert(final Member member);

    MemberWithId findById(final Long id);

    Member findByName(final String name);

    MemberWithId findWithIdByName(final String name);

    List<MemberWithId> findAll();

    boolean existByName(final String memberName);

    Member findMyCouponsByName(final String memberName);
}
