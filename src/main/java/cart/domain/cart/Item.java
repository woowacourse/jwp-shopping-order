package cart.domain.cart;

import cart.domain.VO.Money;
import cart.domain.member.Member;

public interface Item {

    Money calculateTotalPrice();

    void checkOwner(final Member member);

    void changeQuantity(final int quantity);

    Long getId();

    Integer getQuantity();

    Member getMember();

    Product getProduct();
}
