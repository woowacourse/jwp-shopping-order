package cart.member.domain;

import cart.member.repository.MemberEntity;

import java.util.Objects;

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
    
    public static Member from(final MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
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
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(point, member.point);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, point);
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", point=" + point +
                '}';
    }
}
