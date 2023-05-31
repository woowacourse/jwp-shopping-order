package cart.domain2.cart;

import cart.domain2.common.Money;
import cart.domain2.member.Member;

public interface Item {

    Money calculateTotalPrice();

    void checkOwner(final Member member);

    void changeQuantity(final int quantity);

    Long getId();

    Integer getQuantity();

    Member getMember();

    Product getProduct();
}
