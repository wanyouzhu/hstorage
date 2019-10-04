package ltd.highsoft.hkeeper;

import java.time.Instant;

public class AggregateState {

    private final String id;
    private final String state;
    private final Instant timestamp;

    public AggregateState(String id, String state, Instant timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    public String id() {
        return this.id;
    }

    public String content() {
        return state;
    }

    public Instant timestamp() {
        return timestamp;
    }

}
