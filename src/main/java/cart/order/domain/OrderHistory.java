package cart.order.domain;

import cart.member.domain.Member;

import java.time.LocalDateTime;

public class OrderHistory {

    private Long id;
    private Member member;
    private Long totalPrice;
    private LocalDateTime created;

    public OrderHistory(final Member member, final Long totalPrice) {
        this.member = member;
        this.totalPrice = totalPrice;
    }

    public OrderHistory(final Long id, final Member member, final Long totalPrice, final LocalDateTime created) {
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

    public LocalDateTime getCreated() {
        return created;
    }
}
