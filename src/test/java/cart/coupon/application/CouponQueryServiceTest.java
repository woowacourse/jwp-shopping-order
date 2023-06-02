package cart.coupon.application;

import static org.junit.jupiter.api.Assertions.*;

import cart.coupon.application.dto.CouponResponse;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponQueryServiceTest {

  @Autowired
  private CouponQueryService couponQueryService;

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("searchCoupons() : member 가 보유하고 있는 쿠폰 중 사용하지 않은 쿠폰을 조회할 수 있다.")
  void test_searchCoupons() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    //when
    final List<CouponResponse> couponResponses = couponQueryService.searchCoupons(member);

    //then
    assertEquals(2, couponResponses.size());
  }
}
