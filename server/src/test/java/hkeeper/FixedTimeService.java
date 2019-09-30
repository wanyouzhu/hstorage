package hkeeper;

import java.time.Instant;

public class FixedTimeService implements TimeService {

    private final Instant now;

    public FixedTimeService(Instant fixed) {
        this.now = fixed;
    }

    @Override
    public Instant now() {
        return now;
    }

}
