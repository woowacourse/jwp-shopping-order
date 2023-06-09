package cart.fixtures;

import cart.member.domain.Member;

public class MemberFixtures {

    public static class Member_Dooly {
        public static final Long ID = 1L;
        public static final String EMAIL = "dooly@dooly.com";
        public static final String PASSWORD = "1234";
        public static final Long CASH = 1000000L;

        public static final Member ENTITY = Member.of(ID, EMAIL, PASSWORD, CASH);
    }

    public static class Member_Ber {
        public static final Long ID = 2L;
        public static final String EMAIL = "ber@ber.com";
        public static final String PASSWORD = "1234";
        public static final Long CASH = 1000000L;

        public static final Member ENTITY = Member.of(ID, EMAIL, PASSWORD, CASH);
    }

    public static class Member_Bixx {
        public static final Long ID = 2L;
        public static final String EMAIL = "bixx@bixx.com";
        public static final String PASSWORD = "1234";
        public static final Long CASH = 1000000L;

        public static final Member ENTITY = Member.of(ID, EMAIL, PASSWORD, CASH);
    }
}
