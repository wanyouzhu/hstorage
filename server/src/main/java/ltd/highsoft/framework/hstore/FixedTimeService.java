package ltd.highsoft.framework.hstore;

import ltd.highsoft.framework.hstore.TimeService;

import java.time.Instant;

public class FixedTimeService implements TimeService {

    private final Instant fixed;

    FixedTimeService(Instant fixed) {
        this.fixed = fixed;
    }

    @Override
    public Instant now() {
        return fixed;
    }

}
