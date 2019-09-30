package hkeeper;

import java.time.Instant;

public class Entity {

    private final String id;
    private final String state;
    private final Instant timestamp;

    public Entity(String id, String state, Instant timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Entity {id=" + id + ", state=" + state + ", timestamp=" + timestamp + "}";
    }

}
