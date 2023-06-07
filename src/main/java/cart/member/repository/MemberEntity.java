package cart.member.repository;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, point);
    }
}
