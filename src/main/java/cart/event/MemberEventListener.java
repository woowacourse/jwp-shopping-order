package cart.event;

import cart.dao.entity.MemberEntity;
import cart.repository.MemberRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemberEventListener {

    private final MemberRepository memberRepository;

    public MemberEventListener(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @EventListener
    public void updateMember(MemberUpdateEvent event) {
        MemberEntity memberEntity = MemberEntity.toEntity(event.getMember());
        memberRepository.updateMember(memberEntity);
    }
}
