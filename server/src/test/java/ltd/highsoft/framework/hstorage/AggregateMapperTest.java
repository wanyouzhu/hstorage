package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

class AggregateMapperTest {

    private TimeService timeService;
    private AggregateMapper mapper;

    @BeforeEach
    void setUp() {
        timeService = new FixedTimeService(Instant.now());
        mapper = new AggregateMapper(timeService);
    }

    @Test
    void should_be_able_to_map_aggregate_to_state_by_fields() {
        AggregateState expect = new AggregateState("entities", "one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now());
        assertThat(mapper.mapToState(new TestAggregate("one", "van"))).isEqualToComparingFieldByField(expect);
    }

    @Test
    void should_reject_aggregates_without_id_field_while_mapping() {
        Object object = new TypeWithoutIdField("Van");
        Throwable thrown = catchThrowable(() -> mapper.mapToState(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Missing 'id' field in type 'ltd.highsoft.framework.hstorage.TypeWithoutIdField'!");
    }

    @Test
    void should_reject_object_that_can_not_be_serializable() {
        Object object = new NonSerializableObject();
        Throwable thrown = catchThrowable(() -> mapper.mapToState(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping error:");
        assertThat(thrown).hasCauseInstanceOf(JsonProcessingException.class);
    }

    @Test
    void should_be_able_to_map_state_to_aggregate_by_fields() {
        AggregateState state = new AggregateState("entities", "one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now());
        DefaultConstructableAggregate mapped = mapper.mapToAggregate(state, DefaultConstructableAggregate.class);
        assertThat(mapped).hasFieldOrPropertyWithValue("id", "one");
        assertThat(mapped).hasFieldOrPropertyWithValue("name", "van");
    }

    @Test
    void should_be_able_to_map_state_to_aggregate_by_constructor() {
        AggregateState state = new AggregateState("entities", "one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now());
        FullConstructableAggregate mapped = mapper.mapToAggregate(state, FullConstructableAggregate.class);
        assertThat(mapped).isEqualToComparingFieldByField(new FullConstructableAggregate("one", "van"));
    }

    @Test
    void should_reject_malformed_state_during_mapping() {
        AggregateState malformedState = new AggregateState("entities", "one", "{\"id\":\"one\",\"name\":}", timeService.now());
        Throwable thrown = catchThrowable(() -> mapper.mapToAggregate(malformedState, TestAggregate.class));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Malformed state data!");
        assertThat(thrown).hasCauseInstanceOf(IOException.class);
    }

    @Test
    void should_reject_non_deserializable_types_during_mapping() {
        AggregateState state = new AggregateState("entities", "one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now());
        Throwable thrown = catchThrowable(() -> mapper.mapToAggregate(state, AbstractAggregate.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Type 'ltd.highsoft.framework.hstorage.AbstractAggregate' is not constructable!");
        assertThat(thrown).hasCauseInstanceOf(JsonMappingException.class);
    }

}