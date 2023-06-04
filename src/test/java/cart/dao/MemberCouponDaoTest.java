package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.factory.CouponFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
  void saveAndFind() {
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));

    final Long memberCouponId = memberCouponDao.save(member.getId(), couponId);

    final Optional<MemberCoupon> memberCoupon = memberCouponDao.findMemberCouponById(memberCouponId);
    assertThat(memberCoupon).isPresent();
  }

  @Test
  void use() {
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    final Long memberCouponId = memberCouponDao.save(member.getId(), couponId);

    memberCouponDao.use(member.getId(), couponId);

    final MemberCoupon memberCoupon = memberCouponDao.findMemberCouponById(memberCouponId).get();
    assertThat(memberCoupon.isUsed()).isTrue();
  }

  @Test
  void findMemberCouponsByMemberId() {
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId1 = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    final Long couponId2 = couponDao.createCoupon(CouponFactory.createCoupon("2000원 할인", 2000, 20000));
    memberCouponDao.save(member.getId(), couponId1);
    memberCouponDao.save(member.getId(), couponId2);

    final List<MemberCoupon> memberCoupons = memberCouponDao.findMemberCouponsByMemberId(member.getId());
    assertThat(memberCoupons).hasSize(2);
  }

  @Test
  void findAvailableCouponsByMemberIdAndTotalAmount() {
    memberDao.addMember(new Member("email", "password"));
    final Member member = memberDao.getMemberByEmail("email");
    final Long couponId1 = couponDao.createCoupon(CouponFactory.createCoupon("1000원 할인", 1000, 10000));
    final Long couponId2 = couponDao.createCoupon(CouponFactory.createCoupon("1200원 할인", 1200, 12000));
    final Long couponId3 = couponDao.createCoupon(CouponFactory.createCoupon("2000원 할인", 2000, 20000));
    memberCouponDao.save(member.getId(), couponId1);
    memberCouponDao.save(member.getId(), couponId2);
    memberCouponDao.save(member.getId(), couponId3);
    memberCouponDao.use(member.getId(), couponId1);

    final List<MemberCoupon> memberCoupons = memberCouponDao.findAvailableCouponsByMemberIdAndTotalAmount(member.getId(), 15000);

    assertThat(memberCoupons).hasSize(1);
  }
}
