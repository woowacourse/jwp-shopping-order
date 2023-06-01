package cart.domain;

import java.sql.Timestamp;

public interface PointManager {

    Point getPoint(Price price);

    Timestamp getExpiredAt(Timestamp createdAt);
}
