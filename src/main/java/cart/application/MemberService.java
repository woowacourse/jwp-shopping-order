package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.MemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public MemberResponse getRankById(Long id) {
        Member member = memberDao.getMemberById(id);
        return new MemberResponse(id, member.getMemberRank(), member.findDiscountedPercentage());
    }
}
