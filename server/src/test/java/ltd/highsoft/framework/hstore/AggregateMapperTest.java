package ltd.highsoft.framework.hstore;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.*;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        AggregateState expect = new AggregateState("one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now());
        assertThat(mapper.mapToState(new TestAggregate("one", "van"))).isEqualToComparingFieldByField(expect);
    }

    @Test
    void should_reject_aggregates_without_id_field_while_mapping() {
        Object object = new TypeWithoutIdField("Van");
        AbstractThrowableAssert<?, ?> exception = assertThatThrownBy(() -> mapper.mapToState(object));
        exception.isInstanceOf(MappingException.class);
        exception.hasMessage("Missing 'id' field in type 'ltd.highsoft.framework.hstore.TypeWithoutIdField'!");
    }

    @Test
    void should_be_able_to_map_state_to_aggregate_by_fields() {
        AggregateState state = new AggregateState("one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now());
        DefaultConstructableAggregate mapped = mapper.mapToAggregate(state, DefaultConstructableAggregate.class);
        assertThat(mapped).hasFieldOrPropertyWithValue("id", "one");
        assertThat(mapped).hasFieldOrPropertyWithValue("name", "van");
    }

}