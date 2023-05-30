package cart.domain.member;

public class MemberValidator {

    private final Member member;

    public MemberValidator(final Member member) {
        this.member = member;
    }

    public boolean isOwner(final Long id) {
        return member.getId().equals(id);
    }
}
