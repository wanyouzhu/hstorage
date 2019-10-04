package ltd.highsoft.hkeeper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class EntityState {
    private final Object id;
    private final String content;
    private final Instant timestamp;

    public EntityState(Object id, String content, Instant timestamp) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Object id() {
        return this.id;
    }

    public String content() {
        return content;
    }

    public OffsetDateTime timestamp() {
        return timestamp.atOffset(ZoneOffset.UTC);
    }
}
