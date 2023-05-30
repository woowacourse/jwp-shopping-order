package cart.dto;

import java.util.Objects;

public class MemberResponse {
    private Long id;
    private String name;
    private String nickname;

    public MemberResponse() {

    }

    public MemberResponse(final Long id, final String name, final String nickname) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberResponse that = (MemberResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nickname);
    }

    @Override
    public String toString() {
        return "MemberResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
