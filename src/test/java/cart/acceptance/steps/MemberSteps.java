package cart.acceptance.steps;

import cart.domain.member.Member;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberSteps {

    public static void 사용자_저장(final MemberRepository 저장소, final Member 사용자) {
        저장소.save(사용자);
    }
}
