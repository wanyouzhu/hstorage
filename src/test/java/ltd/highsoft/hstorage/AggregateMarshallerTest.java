package ltd.highsoft.hstorage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

class AggregateMarshallerTest {

    private AggregateMarshaller marshaller;

    @BeforeEach
    void setUp() {
        ModelMapping mapping = new ModelMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        marshaller = new AggregateMarshaller(mapping);
    }

    @Test
    void should_be_able_to_marshal_aggregate_to_string_by_fields() {
        String expect = "{\"@type\":\"aggregate\",\"id\":\"one\",\"name\":\"van\"}";
        assertThat(marshaller.marshal(new TestAggregate("one", "van"))).isEqualTo(expect);
    }

    @Test
    void should_reject_object_that_can_not_be_marshalled() {
        Object object = new NonSerializableObject();
        Throwable thrown = catchThrowable(() -> marshaller.marshal(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping error:");
        assertThat(thrown).hasCauseInstanceOf(JsonProcessingException.class);
    }

    @Test
    void should_be_able_to_unmarshal_aggregate_by_fields() {
        String value = "{\"id\":\"one\",\"name\":\"van\"}";
        DefaultConstructableAggregate mapped = marshaller.unmarshal(value, DefaultConstructableAggregate.class);
        assertThat(mapped).hasFieldOrPropertyWithValue("id", "one");
        assertThat(mapped).hasFieldOrPropertyWithValue("name", "van");
    }

    @Test
    void should_be_able_to_unmarshal_aggregate_by_constructor() {
        String value = "{\"id\":\"one\",\"name\":\"van\"}";
        FullConstructableAggregate mapped = marshaller.unmarshal(value, FullConstructableAggregate.class);
        assertThat(mapped).isEqualToComparingFieldByField(new FullConstructableAggregate("one", "van"));
    }

    @Test
    void should_reject_malformed_state_during_unmarshalling() {
        String malformedValue = "{\"id\":\"one\",\"name\":}";
        Throwable thrown = catchThrowable(() -> marshaller.unmarshal(malformedValue, TestAggregate.class));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Malformed state data!");
        assertThat(thrown).hasCauseInstanceOf(IOException.class);
    }

    @Test
    void should_reject_unmarshallable_types_during_unmarshalling() {
        String value = "{\"id\":\"one\",\"name\":\"van\"}";
        Throwable thrown = catchThrowable(() -> marshaller.unmarshal(value, AbstractAggregate.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Type 'ltd.highsoft.hstorage.AbstractAggregate' is not constructable!");
        assertThat(thrown).hasCauseInstanceOf(JsonMappingException.class);
    }

}