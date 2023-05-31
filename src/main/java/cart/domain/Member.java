package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Long point;
    
    public Member(final Long id, final String email, final String password, final Long point) {
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
    
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public void usePoint(final Long usedPoint) {
        this.point -= usedPoint;
    }
    
    public void accumulatePoint(final Long pointToAdd) {
        this.point += pointToAdd;
    }
}
