package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MappingEntryTest {

    @Test
    void should_be_able_to_construct_from_mapping_class() {
        MappingEntry entry = new MappingEntry(TestAggregateMapping.class);
        assertThat(entry.aggregateClass()).isEqualTo(TestAggregate.class);
        assertThat(entry.collection()).isEqualTo("test_aggregates");
        assertThat(entry.mappingClass()).isEqualTo(TestAggregateMapping.class);
    }

}
