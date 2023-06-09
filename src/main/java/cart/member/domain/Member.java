package cart.member.domain;

import java.util.Objects;

public class Member {

  private Long id;
  private String email;
  private String password;

  public Member(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public boolean isMe(final Member member) {
    return this.equals(member);
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

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Member member = (Member) o;
    return Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
