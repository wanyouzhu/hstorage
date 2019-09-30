package hkeeper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import com.google.common.collect.ImmutableMap;

import org.junit.jupiter.api.Test;

class EntityMapperTest {

    private static final Instant NOW = Instant.now();
    private static final String SERIALIZED_ENTITY = "json-from-json-mappser";

    @Test
    void should_map_a_map_to_entity_correctly() {
        TimeService timeService = new FixedTimeService(NOW);
        JsonMapper jsonMapper = new DummyJsonMapper(SERIALIZED_ENTITY);
        EntityMapper entityMapper = new EntityMapper(timeService, jsonMapper);
        Entity entity = entityMapper.map(ImmutableMap.of("id", "1", "name", "zhangsan"));
        assertThat(entity).isEqualToComparingFieldByField(new Entity("1", SERIALIZED_ENTITY, NOW));
    }

}
