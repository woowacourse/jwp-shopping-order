package cart.dto;

public class AuthMember {

    private Long id;
    private String email;

    public AuthMember() {
    }

    public AuthMember(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
