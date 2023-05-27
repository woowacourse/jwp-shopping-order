package cart.repository.mapper;

import cart.dao.entity.MemberEntity;
import cart.domain.Member;

public class MemberMapper {

    public static Member toDomain(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }

    public static MemberEntity toEntity(Member member) {
        return new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getPoint()
        );
    }
}
