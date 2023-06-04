package cart.dto;

public class User {
    private final Long memberId;
    private final String email;

    public User(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }
}
