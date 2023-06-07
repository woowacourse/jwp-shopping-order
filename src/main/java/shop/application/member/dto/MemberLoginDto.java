package shop.application.member.dto;

public class MemberLoginDto {
    private final String name;
    private final String password;

    public MemberLoginDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
