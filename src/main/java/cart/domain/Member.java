package cart.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String email;

    public Member(String email) {
        this(null, email);
    }

    public Member(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.nonNull(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
