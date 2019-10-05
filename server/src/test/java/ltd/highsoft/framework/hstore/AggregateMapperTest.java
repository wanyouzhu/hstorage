package ltd.highsoft.framework.hstore;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;

class AggregateMapperTest {

    @Test
    void should_map_aggregate_to_state_by_fields() {
        TimeService timeService = new FixedTimeService(Instant.now());
        AggregateMapper mapper = new AggregateMapper(timeService);
        AggregateState state = mapper.mapToState(new TestAggregate("one", "van"));
        assertThat(state).isEqualToComparingFieldByField(new AggregateState("one", "{\"id\":\"one\",\"name\":\"van\"}", timeService.now()));
    }

}