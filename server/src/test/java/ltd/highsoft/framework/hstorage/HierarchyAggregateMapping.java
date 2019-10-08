package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.*;

@Mapping(modelClass = BaseAggregate.class, collection = "base_aggregates")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("base-aggregate")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DerivedAggregate.class, name = "derived-aggregate"),
})
class HierarchyAggregateMapping {
}
