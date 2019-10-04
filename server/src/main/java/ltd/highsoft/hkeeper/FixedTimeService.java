package ltd.highsoft.hkeeper;

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
