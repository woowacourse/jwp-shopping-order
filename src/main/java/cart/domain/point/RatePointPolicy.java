package cart.domain.point;

import cart.domain.member.Member;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;

@Component
public class RatePointPolicy implements PointPolicy {

    public static final double EARNING_RATE = 5.0;
    public static final int DURATION_MONTHS = 12;

    @Override
    public long calculateEarnedPoint(Member member, long price) {
        return (long) Math.ceil(price * EARNING_RATE / 100.0);
    }

    @Override
    public Timestamp calculateExpiredAt(Timestamp createAt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createAt);
        calendar.add(Calendar.MONTH, DURATION_MONTHS);
        return new Timestamp(calendar.getTimeInMillis());
    }

    @Override
    public double getEarningRate(Member member) {
        return EARNING_RATE;
    }
}
