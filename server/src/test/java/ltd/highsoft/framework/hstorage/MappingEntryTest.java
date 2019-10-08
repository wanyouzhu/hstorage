package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MappingEntryTest {

    @Test
    void should_be_able_to_construct_from_mapping_class() {
        MappingEntry entry = MappingEntry.ofAggregate(TestAggregateMapping.class);
        assertThat(entry.modelClass()).isEqualTo(TestAggregate.class);
        assertThat(entry.collection()).isEqualTo("test_aggregates");
        assertThat(entry.mappingClass()).isEqualTo(TestAggregateMapping.class);
    }

    @Test
    void should_reject_mapping_classes_without_aggregate_annotation() {
        Throwable thrown = catchThrowable(() -> MappingEntry.ofAggregate(MappingEntryTest.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Invalid mapping class 'ltd.highsoft.framework.hstorage.MappingEntryTest', no '@Aggregate' annotation present!");
    }

    @Test
    void should_reject_mapping_classes_which_has_empty_collection() {
        Throwable thrown = catchThrowable(() -> MappingEntry.ofAggregate(EmptyCollectionMapping.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Invalid mapping class 'ltd.highsoft.framework.hstorage.EmptyCollectionMapping', collection of '@Aggregate' can not be empty!");
    }

    @Test
    void should_be_able_to_construct_from_non_aggregate_mapping_class() {
        MappingEntry entry = MappingEntry.ofNonAggregate(TestNonAggregateMapping.class);
        assertThat(entry.modelClass()).isEqualTo(TestNonAggregate.class);
        assertThat(entry.collection()).isEqualTo(null);
        assertThat(entry.mappingClass()).isEqualTo(TestNonAggregateMapping.class);
    }

    @Test
    void should_reject_mapping_classes_without_non_aggregate_annotation() {
        Throwable thrown = catchThrowable(() -> MappingEntry.ofNonAggregate(MappingEntryTest.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Invalid mapping class 'ltd.highsoft.framework.hstorage.MappingEntryTest', no '@NonAggregate' annotation present!");
    }

}
