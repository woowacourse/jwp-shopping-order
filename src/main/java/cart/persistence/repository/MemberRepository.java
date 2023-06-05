package cart.persistence.repository;

import cart.application.dto.response.OrderItemResponse;
import cart.application.dto.response.OrderResponse;
import cart.domain.member.Member;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.OrderHistoryDao;
import cart.persistence.dao.OrderProductDao;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderHistoryEntity;
import cart.persistence.entity.OrderProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cart.persistence.repository.Mapper.memberMapper;

@Component
public class MemberRepository {

    private final MemberDao memberDao;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;

    public MemberRepository(final MemberDao memberDao, final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao) {
        this.memberDao = memberDao;
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
    }

    public OrderResponse findOrder(final Member member, final Long orderId) {
        final OrderHistoryEntity orderHistoryEntity = orderHistoryDao.findById(orderId);
        if (!orderHistoryEntity.getMemberId().equals(member.getId())) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
        final List<OrderProductEntity> orderProductEntities = orderProductDao.findAllByOrderId(orderId);
        final List<OrderItemResponse> orderItemResponses = orderProductEntities.stream()
                .map(orderProductEntity -> new OrderItemResponse(
                        orderProductEntity.getName(),
                        orderProductEntity.getImageUrl(),
                        orderProductEntity.getQuantity(),
                        orderProductEntity.getPurchasedPrice())
                ).collect(Collectors.toList());
        return new OrderResponse(
                orderItemResponses,
                orderHistoryEntity.getTotalAmount(),
                orderHistoryEntity.getUsedPoint(),
                orderHistoryEntity.getTotalAmount()
        );
    }

    public List<Member> findAllMembers() {
        final List<MemberEntity> members = memberDao.getAllMembers();
        return members.stream()
                .map(Mapper::memberMapper)
                .collect(Collectors.toList());
    }

    public Member findMemberById(final Long memberId) {
        final MemberEntity memberEntity = memberDao.findByMemberId(memberId);
        return memberMapper(memberEntity);
    }
}
