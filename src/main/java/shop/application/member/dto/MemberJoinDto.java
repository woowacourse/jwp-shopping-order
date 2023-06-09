package shop.application.member.dto;

public class MemberJoinDto {
    private final String name;
    private final String password;

    public MemberJoinDto(String name, String password) {
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
