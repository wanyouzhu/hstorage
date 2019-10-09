package ltd.highsoft.infra.hstorage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Mapping(modelClass = BaseAggregate.class, collection = "base_aggregates")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("base-aggregate")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DerivedAggregate.class, name = "derived-aggregate"),
})
class HierarchyAggregateMapping {
}
