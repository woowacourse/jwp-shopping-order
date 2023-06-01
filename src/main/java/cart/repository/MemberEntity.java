package cart.repository;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final Long point;
    
    public MemberEntity(final Long id, final String email, final String password, final Long point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public Long getPoint() {
        return point;
    }
}
