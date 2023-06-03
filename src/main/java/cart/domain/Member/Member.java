package cart.domain.Member;

public class Member {
    private Long id;
    private Email email;
    private Password password;

    private Member(Long id, Email email, Password password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password) {
        this(id, new Email(email), new Password(password));
    }

    public static Member from(String email, String password){
        return new Member(null, Email.from(email), Password.from(password));
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public boolean checkPassword(Password otherPassword) {
        return this.password.equals(otherPassword);
    }
}
