package cart.fixture;

import cart.domain.Member;
import cart.persistence.entity.MemberEntity;

public class MemberFixture {

    public static class Member_test1 {

        private static final String EMAIL = "test1@test.com";
        private static final String PASSWORD = "password1";
        private static final String NICKNAME = "test1";

        public static final MemberEntity ENTITY = new MemberEntity(EMAIL, PASSWORD, NICKNAME);
        public static final Member MEMBER = new Member(EMAIL, PASSWORD, NICKNAME);

        public static MemberEntity getEntityOf(final long id) {
            return new MemberEntity(id, EMAIL, PASSWORD, NICKNAME);
        }

        public static Member getMemberOf(final long id) {
            return new Member(id, EMAIL, PASSWORD, NICKNAME);
        }
    }

    public static class Member_test2 {

        private static final String EMAIL = "test2@test.com";
        private static final String PASSWORD = "password2";
        private static final String NICKNAME = "test2";

        public static final MemberEntity ENTITY = new MemberEntity(EMAIL, PASSWORD, NICKNAME);
        public static final Member MEMBER = new Member(EMAIL, PASSWORD, NICKNAME);

        public static MemberEntity getEntityOf(final long id) {
            return new MemberEntity(id, EMAIL, PASSWORD, NICKNAME);
        }

        public static Member getMemberOf(final long id) {
            return new Member(id, EMAIL, PASSWORD, NICKNAME);
        }
    }
}
