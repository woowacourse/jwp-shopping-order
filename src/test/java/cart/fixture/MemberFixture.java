package cart.fixture;

import cart.domain.Member;
import cart.entity.MemberEntity;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {
    public static final class 주노 {
        private static final long ID = 1L;
        private static final String EMAIL = "junho5336@gmail.com";
        private static final String PASSWORD = "1234";
        private static final Integer MONEY = 1000000;
        private static final Integer POINT = 5000;

        public static final Member MEMBER = new Member(ID, EMAIL, PASSWORD, MONEY, POINT);
        public static final MemberEntity ENTITY = new MemberEntity(ID, EMAIL, PASSWORD, MONEY, POINT);
    }

    public static final class 메리 {
        private static final long ID = 2L;
        private static final String EMAIL = "wonny921@gmail.com";
        private static final String PASSWORD = "1234";
        private static final Integer MONEY = 1000000;
        private static final Integer POINT = 5000;

        public static final Member MEMBER = new Member(ID, EMAIL, PASSWORD, MONEY, POINT);
        public static final MemberEntity ENTITY = new MemberEntity(ID, EMAIL, PASSWORD, MONEY, POINT);
    }

    public static final class 헤나 {
        private static final long ID = 3L;
        private static final String EMAIL = "goatsfish@gmail.com";
        private static final String PASSWORD = "1234";
        private static final Integer MONEY = 1000000;
        private static final Integer POINT = 5000;

        public static final Member MEMBER = new Member(ID, EMAIL, PASSWORD, MONEY, POINT);
        public static final MemberEntity ENTITY = new MemberEntity(ID, EMAIL, PASSWORD, MONEY, POINT);
    }

    public static final class 다니 {
        private static final long ID = 4L;
        private static final String EMAIL = "dani@gmail.com";
        private static final String PASSWORD = "12345";
        private static final Integer MONEY = 1000000;
        private static final Integer POINT = 5000;

        public static final Member MEMBER = new Member(ID, EMAIL, PASSWORD, MONEY, POINT);
        public static final MemberEntity ENTITY = new MemberEntity(ID, EMAIL, PASSWORD, MONEY, POINT);
    }

    public static final class 돈이없어요 {
        private static final long ID = 5L;
        private static final String EMAIL = "oing@gmail.com";
        private static final String PASSWORD = "12345";
        private static final Integer MONEY = 0;
        private static final Integer POINT = 10000;

        public static final Member MEMBER = new Member(ID, EMAIL, PASSWORD, MONEY, POINT);
        public static final MemberEntity ENTITY = new MemberEntity(ID, EMAIL, PASSWORD, MONEY, POINT);
    }


    public static final class 포인트가없어요 {
        private static final long ID = 6L;
        private static final String EMAIL = "oingoing@gmail.com";
        private static final String PASSWORD = "12345";
        private static final Integer MONEY = 100000;
        private static final Integer POINT = 0;

        public static final Member MEMBER = new Member(ID, EMAIL, PASSWORD, MONEY, POINT);
        public static final MemberEntity ENTITY = new MemberEntity(ID, EMAIL, PASSWORD, MONEY, POINT);
    }
}
