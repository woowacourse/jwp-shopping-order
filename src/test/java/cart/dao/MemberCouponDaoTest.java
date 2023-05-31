package cart.dao;

import static cart.fixture.CouponFixture._3만원_이상_3천원_할인_쿠폰_엔티티;
import static cart.fixture.CouponFixture._배달비_3천원_할인_쿠폰_엔티티;
import static cart.fixture.MemberFixture.사용자1_엔티티;
import static cart.fixture.MemberFixture.사용자2_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MemberCouponDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    void 사용자_쿠폰을_저장한다() {
        // given
        final CouponEntity couponEntity = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);
        final MemberEntity memberEntity = memberDao.insert(사용자1_엔티티);
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(
                memberEntity.getId(),
                couponEntity.getId(),
                false
        );

        // when
        memberCouponDao.insert(memberCouponEntity);

        // then
        assertThat(memberCouponDao.findAllByUsedAndMemberId(false, memberCouponEntity.getMemberId())).hasSize(1);
    }

    @Test
    void 사용자_쿠폰을_전부_저장한다() {
        // given
        final CouponEntity couponEntity = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);
        final MemberEntity memberEntity1 = memberDao.insert(사용자1_엔티티);
        final MemberEntity memberEntity2 = memberDao.insert(사용자2_엔티티);
        final MemberCouponEntity memberCouponEntity1 = new MemberCouponEntity(
                memberEntity1.getId(),
                couponEntity.getId(),
                false
        );
        final MemberCouponEntity memberCouponEntity2 = new MemberCouponEntity(
                memberEntity2.getId(),
                couponEntity.getId(),
                false
        );

        // when
        memberCouponDao.insertAll(List.of(memberCouponEntity1, memberCouponEntity2));

        // then
        assertAll(
                () -> assertThat(memberCouponDao.findAllByUsedAndMemberId(false, memberCouponEntity1.getMemberId()))
                        .hasSize(1),
                () -> assertThat(memberCouponDao.findAllByUsedAndMemberId(false, memberCouponEntity2.getMemberId()))
                        .hasSize(1)
        );
    }

    @Test
    void 사용자_쿠폰을_수정한다() {
        // given
        final CouponEntity couponEntity = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);
        final MemberEntity memberEntity = memberDao.insert(사용자1_엔티티);
        final MemberCouponEntity memberCouponEntity = memberCouponDao.insert(new MemberCouponEntity(
                memberEntity.getId(),
                couponEntity.getId(),
                false
        ));
        final MemberCouponEntity updateMemberCouponEntity = new MemberCouponEntity(
                memberEntity.getId(),
                couponEntity.getId(),
                true
        );

        // when
        memberCouponDao.update(updateMemberCouponEntity);

        // then
        assertThat(memberCouponDao.findAllByUsedAndMemberId(false, memberEntity.getId())).isEmpty();
    }

    @Test
    void 사용자_아이디를_입력받아_사용하지_않은_사용자의_쿠폰_엔티티를_조회한다() {
        // given
        final CouponEntity couponEntity1 = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);
        final CouponEntity couponEntity2 = couponDao.insert(_배달비_3천원_할인_쿠폰_엔티티);
        final CouponEntity couponEntity3 = couponDao.insert(_배달비_3천원_할인_쿠폰_엔티티);
        final MemberEntity memberEntity = memberDao.insert(사용자1_엔티티);
        final MemberCouponEntity memberCouponEntity1 = memberCouponDao.insert(new MemberCouponEntity(
                memberEntity.getId(),
                couponEntity1.getId(),
                false
        ));
        final MemberCouponEntity memberCouponEntity2 = memberCouponDao.insert(new MemberCouponEntity(
                memberEntity.getId(),
                couponEntity2.getId(),
                false
        ));
        memberCouponDao.insert(new MemberCouponEntity(memberEntity.getId(), couponEntity3.getId(), true));

        // when
        final List<MemberCouponEntity> result = memberCouponDao.findAllByUsedAndMemberId(false, memberEntity.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(memberCouponEntity1, memberCouponEntity2));
    }
}
