package cart.domain;

public class AuthMember {

    private final Member member;
    private final String password;

    public AuthMember(Member member, String password) {
        this.member = member;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return member.getId();
    }

    public Member getMember() {
        return member;
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getPassword() {
        return password;
    }
}
