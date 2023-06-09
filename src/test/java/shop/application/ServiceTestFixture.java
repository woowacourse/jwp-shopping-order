package shop.application;

import shop.domain.member.Member;
import shop.domain.member.MemberName;
import shop.domain.member.Password;
import shop.domain.product.Product;

public class ServiceTestFixture {
    static final Product pizza = new Product("피자", 8000, "피자.com");
    static final Product chicken = new Product("치킨", 30000, "치킨.com");
    static final Member member = new Member(new MemberName("쥬니이름"), Password.createFromNaturalPassword("쥬니비번"));
}
