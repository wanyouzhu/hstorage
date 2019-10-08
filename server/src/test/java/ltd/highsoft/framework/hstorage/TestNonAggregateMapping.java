package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.*;

@Mapping(modelClass = TestNonAggregate.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("aggregate")
class TestNonAggregateMapping {
}
