package cart.persistence.entity;

public class MemberEntity {

    private final Long id;
    private final String name;
    private final String password;

    public MemberEntity(final String name, final String password) {
        this(null, name, password);
    }

    public MemberEntity(final Long id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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
