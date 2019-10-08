package ltd.highsoft.framework.hstorage;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

class ModelMappingTest {

    @Test
    void should_be_able_to_specify_mapping_entries() {
        ImmutableList<MappingEntry> entries = ImmutableList.of(new MappingEntry(TestAggregateMapping.class));
        ModelMapping mapping = new ModelMapping(entries);
        assertThat(mapping.collectionOf(TestAggregate.class)).isEqualTo("test_aggregates");
        assertThat(ImmutableList.copyOf(mapping.entries())).isEqualTo(entries);
    }

    @Test
    void should_be_able_to_retrieve_id_from_aggregate() {
        ModelMapping mapping = new ModelMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        TestAggregate aggregate = new TestAggregate("one", "van");
        assertThat(mapping.idOf(aggregate)).isEqualTo("one");
    }

    @Test
    void should_reject_aggregates_without_id_field_during_id_retrieving() {
        ModelMapping mapping = new ModelMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        Object object = new TypeWithoutIdField("Van");
        Throwable thrown = catchThrowable(() -> mapping.idOf(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Missing 'id' field in type 'ltd.highsoft.framework.hstorage.TypeWithoutIdField'!");
    }

    @Test
    void should_throw_mapping_exception_if_no_mapping_entry_present_while_resolving_collection() {
        ModelMapping mapping = new ModelMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        Throwable thrown = catchThrowable(() -> mapping.collectionOf(String.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping not found for aggregate class 'java.lang.String'!");
    }

    @Test
    void should_reject_duplicated_mapping_entries_while_constructing() {
        MappingEntry entryOne = new MappingEntry(TestAggregateMapping.class);
        MappingEntry entryTwo = new MappingEntry(AnotherTestAggregateMapping.class);
        Throwable thrown = catchThrowable(() -> new ModelMapping(ImmutableList.of(entryOne, entryTwo)));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessageContaining("Multiple mapping specified for aggregate class 'ltd.highsoft.framework.hstorage.TestAggregate': ");
        assertThat(thrown).hasMessageContaining("ltd.highsoft.framework.hstorage.TestAggregateMapping");
        assertThat(thrown).hasMessageContaining("ltd.highsoft.framework.hstorage.AnotherTestAggregateMapping");
    }

    @Test
    void should_be_able_to_retrieve_collection_for_sub_classes() {
        ImmutableList<MappingEntry> entries = ImmutableList.of(new MappingEntry(HierarchyAggregateMapping.class));
        ModelMapping mapping = new ModelMapping(entries);
        assertThat(mapping.collectionOf(DerivedAggregate.class)).isEqualTo("base_aggregates");
    }

    @Test
    void should_throw_exception_while_attempting_to_retrieve_collection_for_non_aggregate_mappings() {
        ImmutableList<MappingEntry> entries = ImmutableList.of(new MappingEntry(TestNonAggregateMapping.class));
        ModelMapping mapping = new ModelMapping(entries);
        Throwable thrown = catchThrowable(() -> mapping.collectionOf(TestNonAggregate.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessageContaining("Class 'ltd.highsoft.framework.hstorage.TestNonAggregate' is mapped as a non-aggregate!");
    }

}
