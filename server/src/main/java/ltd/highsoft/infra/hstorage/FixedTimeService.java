package ltd.highsoft.infra.hstorage;

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
