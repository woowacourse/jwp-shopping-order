package cart.repository.mapper;

import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toDomain(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }

    public MemberEntity toEntity(Member member) {
        return new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getPoint()
        );
    }
}
