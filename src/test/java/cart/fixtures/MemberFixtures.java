package cart.fixtures;

import cart.domain.member.domain.Member;

public class MemberFixtures {

    public static class Dooly {
        public static final Long ID = 1L;
        public static final String EMAIL = "dooly@dooly.com";
        public static final String PASSWORD = "1234";
        public static final int CASH = 5000;

        public static Member ENTITY() {
           return new Member(ID, EMAIL, PASSWORD);
        }
    }

    public static class Ber {
        public static final Long ID = 2L;
        public static final String EMAIL = "ber@ber.com";
        public static final String PASSWORD = "1234";
        public static final int CASH = 5000;
        public static Member ENTITY() {
            return new Member(ID, EMAIL, PASSWORD);
        }
    }

    public static class Bixx {
        public static final Long ID = 3L;
        public static final String EMAIL = "bixx@bixx.com";
        public static final String PASSWORD = "1234";
        public static final int CASH = 5000;
        public static Member ENTITY() {
            return new Member(ID, EMAIL, PASSWORD);
        }
    }
}
