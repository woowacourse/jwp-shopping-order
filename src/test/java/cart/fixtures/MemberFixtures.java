package cart.fixtures;

import cart.domain.Member;

public class MemberFixtures {

    public static class MemberA {
        public static final Long ID = 1L;
        public static final String EMAIL = "a@a.com";
        public static final String PASSWORD = "1234";

        public static final Member ENTITY = new Member(ID, EMAIL, PASSWORD);
    }

    public static class MemberB {
        public static final Long ID = 2L;
        public static final String EMAIL = "b@b.com";
        public static final String PASSWORD = "1234";

        public static final Member ENTITY = new Member(ID, EMAIL, PASSWORD);
    }
}
