package ltd.highsoft.infra.hstorage.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import ltd.highsoft.infra.hstorage.Mapping;
import ltd.highsoft.infra.hstorage.TestAggregate;

@Mapping(modelClass = TestAggregate.class, collection = "test_aggregates")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("aggregate")
public class MappingOne {
}
