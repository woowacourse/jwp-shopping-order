package cart.fixture;

import cart.domain.member.Member;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Member 디노 = new Member("디노", "dino@gmail.com", "dino123");
    public static Member 비버 = new Member("비버", "beaver@gmail.com", "beaver123");
    public static Member 레오 = new Member("레오", "b@b.com", "1234");
    public static Member 나루토= new Member("나루토", "na@b.com", "1234");

    public static Member 디노_ID포함 = new Member(6L, "디노", "dino@gmail.com", "dino123");
    public static Member 비버_ID포함 = new Member(2L, "비버", "beaver@gmail.com", "beaver123");
    public static Member 레오_ID포함 = new Member(1L, "레오강아지", "a@a.com", "1234");

}
