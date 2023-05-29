package cart.domain.member;

public class Member {
    private Long id;
    private MemberName name;
    private Password password;

    public Member(Long id, String name, String password) {
        this.id = id;
        this.name = new MemberName(name);
        this.password = new Password(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public boolean checkPassword(String password) {
        return this.password.getPassword().equals(password);
    }
}
