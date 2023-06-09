package cart.entity;

import cart.domain.AuthMember;

public class AuthMemberEntity {

    private final MemberEntity memberEntity;
    private final String password;

    public AuthMemberEntity(MemberEntity memberEntity, String password) {
        this.memberEntity = memberEntity;
        this.password = password;
    }

    public AuthMember toDomain() {
        return new AuthMember(memberEntity.toDomain(), password);
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public String getPassword() {
        return password;
    }
}
