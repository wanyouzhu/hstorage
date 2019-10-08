package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.*;

@Mapping(modelClass = TestAggregate.class, collection = "test_aggregates")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("aggregate")
class TestAggregateMapping {
}
