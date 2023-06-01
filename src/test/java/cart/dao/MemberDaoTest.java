package cart.dao;

import cart.domain.Member;
import cart.domain.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import({MemberDao.class})
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("멤버 정보를 업데이트 한다.")
    void update_member() {
        // given
        Member member = new Member(null, "ako@wooteco.com", "Abcd1234@", Rank.NORMAL, 0);
        Long memberId = memberDao.addMember(member);

        Member savedMember = memberDao.getMemberById(memberId);
        int purchaseAmount = 200_001;
        savedMember.update(purchaseAmount);

        // when
        memberDao.updateMember(savedMember);
        Member result = memberDao.getMemberById(memberId);

        // then
        assertThat(result.getRank()).isEqualTo(Rank.GOLD);
        assertThat(result.getTotalPurchaseAmount()).isEqualTo(purchaseAmount);
    }
}