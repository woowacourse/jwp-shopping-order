package cart.ui;

import cart.domain.Member;

public interface MemberAuthenticator {
    Member findAuthenticatedMember(String credential);
}
