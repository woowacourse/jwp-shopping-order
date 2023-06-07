package cart.entity;

import cart.domain.Member;
import java.util.Objects;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final int point;

    public MemberEntity(final Long id, final String email, final String password) {
        this(id, email, password, 0);
    }

    public MemberEntity(final Long id, final String email, final String password, final int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public static MemberEntity from(Member member) {
        return new MemberEntity(member.getId(), member.getEmail(), member.getPassword(), member.getPoint());
    }

    public static Member toDomain(MemberEntity memberEntity) {
        return new Member(
                memberEntity.id,
                memberEntity.email,
                memberEntity.password,
                memberEntity.point
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

    public int getPoint() {
        return point;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberEntity that = (MemberEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
