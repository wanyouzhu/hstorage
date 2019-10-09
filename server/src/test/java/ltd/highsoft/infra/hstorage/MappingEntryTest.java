package ltd.highsoft.infra.hstorage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MappingEntryTest {

    @Test
    void should_be_able_to_construct_from_mapping_class() {
        MappingEntry entry = new MappingEntry(TestAggregateMapping.class);
        assertThat(entry.modelClass()).isEqualTo(TestAggregate.class);
        assertThat(entry.collection()).isEqualTo("test_aggregates");
        assertThat(entry.mappingClass()).isEqualTo(TestAggregateMapping.class);
    }

    @Test
    void should_reject_mapping_classes_without_mapping_annotation() {
        Throwable thrown = catchThrowable(() -> new MappingEntry(MappingEntryTest.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Invalid mapping class 'ltd.highsoft.infra.hstorage.MappingEntryTest', no '@Mapping' annotation present!");
    }

    @Test
    void should_be_able_to_construct_from_non_aggregate_mapping_class() {
        MappingEntry entry = new MappingEntry(TestNonAggregateMapping.class);
        assertThat(entry.modelClass()).isEqualTo(TestNonAggregate.class);
        assertThat(entry.collection()).isEqualTo("");
        assertThat(entry.mappingClass()).isEqualTo(TestNonAggregateMapping.class);
    }

}
