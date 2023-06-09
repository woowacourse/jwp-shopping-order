package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.factory.CouponFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberCouponDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private MemberCouponDao memberCouponDao;

  private MemberDao memberDao;

  private CouponDao couponDao;

  @BeforeEach
  void setUp() {
    memberCouponDao = new MemberCouponDao(jdbcTemplate);
    memberDao = new MemberDao(jdbcTemplate);
    couponDao = new CouponDao(jdbcTemplate);
  }

  @Test
  @DisplayName("멤버가 가지고 있는 쿠폰을 저장하거나 찾을 수 있다.")
  void saveAndFind() {
    //given
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));

    //when
    final Long memberCouponId = memberCouponDao.save(member.getId(), couponId);

    //then
    final Optional<MemberCoupon> memberCoupon = memberCouponDao.findMemberCouponById(memberCouponId);
    assertThat(memberCoupon).isPresent();
  }

  @Test
  @DisplayName("쿠폰을 사용할 수 있다.")
  void updateIsUsed() {
    //given
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    final Long memberCouponId = memberCouponDao.save(member.getId(), couponId);

    //when
    memberCouponDao.updateIsUsed(member.getId(), couponId);

    //then
    final MemberCoupon memberCoupon = memberCouponDao.findMemberCouponById(memberCouponId).get();
    assertThat(memberCoupon.isUsed()).isTrue();
  }

  @Test
  @DisplayName("MemberId로 멤버가 가지고 있는 쿠폰들을 찾을 수 있다.")
  void findMemberCouponsByMemberId() {
    //given
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId1 = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    final Long couponId2 = couponDao.createCoupon(CouponFactory.createCoupon("2000원 할인", 2000, 20000));
    memberCouponDao.save(member.getId(), couponId1);
    memberCouponDao.save(member.getId(), couponId2);

    //when
    final List<MemberCoupon> memberCoupons = memberCouponDao.findMemberCouponsByMemberId(member.getId());

    //hen
    assertThat(memberCoupons).hasSize(2);
  }

  @Test
  @DisplayName("사용 가능한 쿠폰들을 찾을 수 있다.")
  void findAvailableCouponsByMemberIdAndTotalAmount() {
    //given
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId1 = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    final Long couponId2 = couponDao.createCoupon(CouponFactory.createCoupon("1200원 할인", 1200, 12000));
    final Long couponId3 = couponDao.createCoupon(CouponFactory.createCoupon("2000원 할인", 2000, 20000));
    memberCouponDao.save(member.getId(), couponId1);
    memberCouponDao.save(member.getId(), couponId2);
    memberCouponDao.save(member.getId(), couponId3);
    memberCouponDao.updateIsUsed(member.getId(), couponId1);

    //when
    final List<MemberCoupon> memberCoupons = memberCouponDao.findAvailableCouponsByMemberIdAndTotalAmount(member.getId(), 15000);

    //then
    assertThat(memberCoupons).hasSize(1);
  }

  @Test
  @DisplayName("멤버가 쿠폰을 가지고 있는지 확인 할 수 있다.")
  void existsByMemberIdAndCouponId() {
    //given
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    memberCouponDao.save(member.getId(), couponId);

    //when
    final boolean isExist = memberCouponDao.existsByMemberIdAndCouponId(member.getId(), couponId);

    //then
    assertThat(isExist).isTrue();
  }
}
