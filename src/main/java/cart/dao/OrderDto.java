package cart.dao;

import java.time.LocalDateTime;

public class OrderDto {

    private final Long id;
    private final Long memberId;
    private final LocalDateTime timeStamp;

    public OrderDto(final Long id, final Long memberId, final LocalDateTime timeStamp) {
        this.id = id;
        this.memberId = memberId;
        this.timeStamp = timeStamp;
    }

    public OrderDto(final Long memberId, final LocalDateTime timeStamp) {
        this(null, memberId, timeStamp);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

}
