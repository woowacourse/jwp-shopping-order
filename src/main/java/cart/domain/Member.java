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
        if (usedPoint < 0 || usedPoint > this.point) {
            throw new IllegalArgumentException("사용할 포인트가 현재 보유중인 포인트의 범위를 넘었습니다. 현재 보유 포인트 : " + this.point + ", 사용할 포인트 : " + usedPoint);
        }
        this.point -= usedPoint;
    }
    
    public void accumulatePoint(final Long pointToAdd) {
        this.point += pointToAdd;
    }
}
