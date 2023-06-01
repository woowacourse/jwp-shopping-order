package cart.domain;

import java.util.Date;

public class CartOrder {

    private Long id;
    private Member member;
    private Long totalPrice;
    private Date created;

    public CartOrder(final Member member, final Long totalPrice) {
        this.member = member;
        this.totalPrice = totalPrice;
    }

    public CartOrder(final Long id, final Member member, final Long totalPrice, final Date created) {
        this.id = id;
        this.member = member;
        this.totalPrice = totalPrice;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Date getCreated() {
        return created;
    }
}
