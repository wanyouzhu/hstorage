package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Mapping(modelClass = TestNonAggregate.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("aggregate")
class TestNonAggregateMapping {
}
