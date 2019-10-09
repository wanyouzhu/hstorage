package ltd.highsoft.infra.hstorage;

import io.micrometer.core.instrument.util.StringUtils;

import java.time.Instant;

public class AggregateState {

    private final String collection;
    private final String id;
    private final String state;
    private final Instant timestamp;

    public AggregateState(String collection, String id, String state, Instant timestamp) {
        this.collection = collection;
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        validate();
    }

    private void validate() {
        validateCollection();
        validateId();
        validateState();
        validateTimestamp();
    }

    private void validateCollection() {
        if (StringUtils.isBlank(collection)) {
            throw new MalformedDataException("Aggregate collection is missing!");
        }
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
        return collection;
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
