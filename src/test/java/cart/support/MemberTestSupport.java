package cart.support;

import cart.dao.MemberDao;
import cart.domain.Member;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MemberTestSupport {

    private static String defaultEmail = "member";
    private static String defaultPassword = "password";
    private static int defaultPoint = 5_000;

    private final MemberDao memberDao;

    public MemberTestSupport(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberBuilder builder() {
        return new MemberBuilder();
    }

    public final class MemberBuilder {

        private Long id;
        private String email;
        private String password;
        private Integer point;

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder point(Integer point) {
            this.point = point;
            return this;
        }

        public Member build() {
            Member member = make();
            Long memberId = memberDao.insert(member);
            return new Member(memberId, member.getEmail(), member.getPassword(), member.getPointAsInt());
        }

        public Member make() {
            return new Member(
                    id == null ? null : id,
                    email == null ? defaultEmail + UUID.randomUUID() : email,
                    password == null ? defaultPassword : password,
                    point == null ? defaultPoint : point);
        }
    }
}
