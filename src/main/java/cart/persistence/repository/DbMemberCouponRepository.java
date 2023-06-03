package cart.persistence.repository;

import cart.domain.memberCoupon.MemberCoupon;
import cart.domain.memberCoupon.MemberCouponRepository;
import cart.exception.NoSuchCouponException;
import cart.exception.NoSuchMemberCouponException;
import cart.exception.NoSuchMemberException;
import cart.persistence.dao.CouponDao;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.CouponMapper;
import cart.persistence.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbMemberCouponRepository implements MemberCouponRepository {
    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;
    private final MemberDao memberDao;

    public DbMemberCouponRepository(MemberCouponDao memberCouponDao, CouponDao couponDao, MemberDao memberDao) {
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
        this.memberDao = memberDao;
    }

    @Override
    public List<MemberCoupon> findAll() {
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findAll();
        return memberCouponEntities.stream()
                .map(this::mapToMemberCoupon)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberCoupon> findMemberCouponsByMemberId(Long memberId) {
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findMemberCouponsByMemberId(memberId);
        return memberCouponEntities.stream()
                .map(this::mapToMemberCoupon)
                .collect(Collectors.toList());
    }

    @Override
    public MemberCoupon findByCouponId(Long couponId) {
        MemberCouponEntity memberCouponEntity = memberCouponDao.findOneByCouponId(couponId).orElseThrow(() -> new NoSuchMemberCouponException());
        return mapToMemberCoupon(memberCouponEntity);
    }

    @Override
    public MemberCoupon findById(Long id) {
        MemberCouponEntity memberCouponEntity = memberCouponDao.findById(id).orElseThrow(() -> new NoSuchMemberCouponException());
        return mapToMemberCoupon(memberCouponEntity);
    }

    @Override
    public Long add(MemberCoupon memberCoupon) {
        return memberCouponDao.add(mapToMemberCouponEntity(memberCoupon));
    }

    @Override
    public void delete(Long id) {
        memberCouponDao.delete(id);
    }

    private MemberCouponEntity mapToMemberCouponEntity(MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getId(),
                memberCoupon.getMember().getId(),
                memberCoupon.getCoupon().getId());
    }

    private MemberCoupon mapToMemberCoupon(MemberCouponEntity memberCouponEntity) {
        MemberEntity memberEntity = memberDao.findById(memberCouponEntity.getMemberId()).orElseThrow(() -> new NoSuchMemberException());
        CouponEntity couponEntity = couponDao.findById(memberCouponEntity.getCouponId()).orElseThrow(() -> new NoSuchCouponException());

        return new MemberCoupon(
                memberCouponEntity.getId(),
                MemberMapper.toDomain(memberEntity),
                CouponMapper.toDomain(couponEntity)
        );
    }
}
