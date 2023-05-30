package cart.fixture;

import cart.domain.Member;
import cart.dto.MemberCreateRequest;

public class MemberFixtures {

    public static final Member MEMBER_GITCHAN = new Member(1L, "gitchan@wooteco.com", "password!");
    public static final MemberCreateRequest CREATE_REQUEST_GITCHAN = MemberCreateRequest.from(MEMBER_GITCHAN);
    public static final Member MEMBER_IRENE = new Member(2L, "이리내", "irene@wooteco.com");
}
