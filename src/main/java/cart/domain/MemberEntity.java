package cart.domain;

import java.util.Objects;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final int point;

    public MemberEntity(final Long id, final String email, final String password, final int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public MemberEntity(final String email, final String password, final int point) {
        this(null, email, password, point);
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

    public int getPoint() {
        return point;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberEntity that = (MemberEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
