package cart.Repository;

import cart.domain.Member.Member;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member toMember(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword()
        );
    }
}
