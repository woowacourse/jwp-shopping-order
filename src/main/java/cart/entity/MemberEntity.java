package cart.entity;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer gradeId;

    public MemberEntity(Long id, String email, String password, Integer gradeId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.gradeId = gradeId;
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

    public Integer getGradeId() {
        return gradeId;
    }
}
