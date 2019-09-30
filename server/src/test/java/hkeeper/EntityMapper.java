package hkeeper;

import java.time.Instant;
import java.util.Map;

public class EntityMapper {

    private final TimeService timeService;
    private final JsonMapper jsonMapper;

    public EntityMapper(TimeService timeService, JsonMapper jsonMapper) {
        this.timeService = timeService;
        this.jsonMapper = jsonMapper;
    }

    public Entity map(Map<String, Object> source) {
        final String id = source.get("id").toString();
        final String state = jsonMapper.toJson(source);
        final Instant timestamp = timeService.now();
        return new Entity(id, state, timestamp);
    }

}
