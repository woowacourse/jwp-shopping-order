package cart.persistence.entity;

public class MemberEntity {
    private final Long id;
    private final String name;
    private final String password;

    public MemberEntity(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public MemberEntity(String name, String password) {
        this(null, name, password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
