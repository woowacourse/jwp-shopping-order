package cart.domain.member;

public class MemberName {
    private final String memberName;

    private MemberName(String memberName) {
        validate(memberName);
        this.memberName = memberName;
    }

    private void validate(String memberName) {
        if (memberName.length() < 1 || memberName.length() > 30) {
            throw new IllegalArgumentException("멤버의 이름은 1 ~ 30글자 사이만 가능합니다.");
        }
    }

    public static MemberName from(String memberName) {
        return new MemberName(memberName);
    }

    public String getMemberName() {
        return memberName;
    }
}
