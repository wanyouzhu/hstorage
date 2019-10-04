package ltd.highsoft.hkeeper;

import java.time.Instant;

public class EntityState {

    private final String id;
    private final String content;
    private final Instant timestamp;

    public EntityState(String id, String content, Instant timestamp) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String id() {
        return this.id;
    }

    public String content() {
        return content;
    }

    public Instant timestamp() {
        return timestamp;
    }

}
