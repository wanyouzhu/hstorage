package ltd.highsoft.infra.hstorage;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class AggregateStateTest {

    @Test
    void should_reject_blank_collection_while_constructing() {
        Throwable thrown = catchThrowable(() -> new AggregateState("", "one", "{}", Instant.now()));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Aggregate collection is missing!");
    }

    @Test
    void should_reject_blank_id_while_constructing() {
        Throwable thrown = catchThrowable(() -> new AggregateState("entities", "", "{}", Instant.now()));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Aggregate ID is missing!");
    }

    @Test
    void should_reject_blank_state_while_constructing() {
        Throwable thrown = catchThrowable(() -> new AggregateState("entities", "one", "", Instant.now()));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Aggregate state is missing!");
    }

    @Test
    void should_reject_null_timestamp_while_constructing() {
        Throwable thrown = catchThrowable(() -> new AggregateState("entities", "one", "{}", null));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Aggregate timestamp is missing!");
    }

}