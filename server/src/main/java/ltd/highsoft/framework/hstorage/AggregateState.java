package ltd.highsoft.framework.hstorage;

import io.micrometer.core.instrument.util.StringUtils;

import java.time.Instant;

public class AggregateState {

    private final String id;
    private final String state;
    private final Instant timestamp;

    public AggregateState(String collection, String id, String state, Instant timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        validate();
    }

    public AggregateState(String id, String state, Instant timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        validate();
    }

    private void validate() {
        validateId();
        validateState();
        validateTimestamp();
    }

    private void validateId() {
        if (StringUtils.isBlank(id)) {
            throw new MalformedDataException("Aggregate ID is missing!");
        }
    }

    private void validateState() {
        if (StringUtils.isBlank(state)) {
            throw new MalformedDataException("Aggregate state is missing!");
        }
    }

    private void validateTimestamp() {
        if (timestamp == null) {
            throw new MalformedDataException("Aggregate timestamp is missing!");
        }
    }

    public String collection() {
        return "entities"; // FIXME
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
