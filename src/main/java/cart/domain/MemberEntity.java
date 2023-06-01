package cart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final int point;

    public MemberEntity(final String email, final String password, final int point) {
        this(null, email, password, point);
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
